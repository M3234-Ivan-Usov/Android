package app.com.animation.viewer

import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.text.getSpans
import app.com.animation.R
import java.util.*

class CppParser(
    var raw: String, private var resources: Resources,
    var name: String, var fromGit: Boolean
) {
    private var code = SpannableStringBuilder(raw)
    private var structures = HashSet<String>()
    private var defines = HashSet<String>()
    private var typenames = HashMap<String, Int>()
    var linesAmount = 0
    private var parsed = false

    private var afterStruct = false
    private var afterTypedef = false
    private var afterDefine = false
    private var preproc = false
    private var lineComment = false
    private var afterInclude = false
    private var afterTypename = false

    private var lastWordStart = 0
    private var lastWordEnd = 0
    private var brackets : Int = 0

    companion object {
        private val KEY_WORDS = setOf(
            "class", "const_cast", "decltype", "default", "delete", "dynamic_cast",
            "extern", "namespace", "new", "noexcept", "operator", "private", "public",
            "reinterpret_cast", "sizeof", "static_cast", "struct", "template",
            "typedef", "typeid", "typename", "union", "using", "virtual", "protected"
        )

        private val PRIMTIVE_TYPES = setOf(
            "bool", "char", "double", "float", "int", "long", "short", "void", "wchar_t"
        )

        private val MODIFIERS = setOf(
            "auto", "const", "constexpr", "explicit", "friend", "inline",
            "mutable", "signed", "static", "unsigned", "volatile"
        )

        private val MANAGING = setOf(
            "break", "case", "catch", "continue", "else", "for", "goto", "if", "return",
            "static_assert", "switch", "throw", "try", "while", "do"
        )

        private val CONSTANTS = setOf("false", "true", "nullptr", "this", "NULL")

        private val PREPROCESSOR = setOf(
            "define", "elif", "else", "endif", "error", "if", "ifdef", "ifndef",
            "import", "include", "line", "pragma", "undef", "using", "#define",
            "#elif", "#else", "#endif", "#error", "#if", "#ifdef", "#ifndef",
            "#import", "#include", "#line", "#pragma", "#undef", "#using"
        )


        private val DELIMETERS = setOf(
            ' ', ';', '(', ')', '{', '}', '+', '-',
            '*', '/', '*', '<', '>', '=', ',', '[', ']', '&', ':'
        )

        private const val NO_HIGHLIGHT = "No highlight here"
        private const val LINE_COMMENT = "Line comment here"
        private const val STRING = "String here"

    }

    fun parse(): SpannableStringBuilder {
        if (!parsed) {
            val lines = raw.split("\n")
            linesAmount = lines.size
            var offset = 0
            for (line in lines) {
                var position = 0
                loop@ while (position < line.length) {
                    val next = nextToken(position, line)
                    when (next.first) {
                        LINE_COMMENT -> {
                            highlight(R.color.cpp_preprocessor, position, line.length, offset)
                            break@loop
                        }
                        STRING -> {
                            afterInclude = false
                            highlight(R.color.cpp_string, position, next.second, offset)
                        }
                        NO_HIGHLIGHT -> {
                        }
                        else -> match(next.first, position, next.second, offset)
                    }
                    position = next.second
                    if (position == line.length && afterTypedef) {
                        afterTypedef = false
                        structures.add(line.substring(lastWordStart, lastWordEnd))
                        val x = code.getSpans<Any>(offset + lastWordStart, offset + lastWordEnd)
                        code.removeSpan(x[0])
                        highlight(R.color.cpp_user_type, lastWordStart, lastWordEnd, offset)
                    }
                }
                lineComment = false
                offset += line.length + 1
            }
            parsed = true
        }
        return code
    }

    private fun match(lexem: String, start: Int, end: Int, offset: Int) {
        if (!matchKeyWord(lexem, start, end, offset)) {
            if (!matchFlags(lexem, start, end, offset)) {
                highlight(R.color.cpp_default, start, end, offset)
            }
        }
    }

    private fun matchKeyWord(lexem: String, start: Int, end: Int
                             , offset: Int): Boolean {
        if (KEY_WORDS.contains(lexem)) {
            when (lexem) {
                "class", "struct" -> afterStruct = true
                "typedef" -> afterTypedef = true
                "typename" -> afterTypename = true
            }
            highlight(R.color.cpp_key_word, start, end, offset)
            return true
        }
        if (PRIMTIVE_TYPES.contains(lexem)) {
            highlight(R.color.cpp_primitive, start, end, offset)
            return true
        }
        if (MODIFIERS.contains(lexem)) {
            highlight(R.color.cpp_modifier, start, end, offset)
            return true
        }
        if (CONSTANTS.contains(lexem)) {
            highlight(R.color.cpp_constant, start, end, offset)
            return true
        }
        if (MANAGING.contains(lexem)) {
            highlight(R.color.cpp_managing, start, end, offset)
            return true
        }
        if (structures.contains(lexem)) {
            highlight(R.color.cpp_user_type, start, end, offset)
            afterStruct = false
            return true
        }
        if (defines.contains(lexem)) {
            highlight(R.color.cpp_macros, start, end, offset)
            return true
        }

        if (typenames.contains(lexem)) {
            highlight(R.color.colorAccent, start, end, offset)
            return true
        }
        return false
    }

    private fun matchFlags(lexem: String, start: Int, end: Int, offset: Int): Boolean {
        if (lexem.toDoubleOrNull() != null) {
            highlight(R.color.cpp_number, start, end, offset)
            return true
        }

        if (afterStruct) {
            afterStruct = false
            highlight(R.color.cpp_user_type, start, end, offset)
            structures.add(lexem)
            return true
        }
        if (preproc) {
            if (PREPROCESSOR.contains(lexem)) {
                preproc = false
            }
            when (lexem) {
                "define", "#define" -> afterDefine = true
                "include", "#include" -> afterInclude = true
            }
            highlight(R.color.cpp_preprocessor, start, end, offset)
            return true
        }

        if (afterDefine) {
            afterDefine = false
            defines.add(lexem)
            highlight(R.color.cpp_macros, start, end, offset)
            return true
        }

        if (afterInclude) {
            afterInclude = false
            highlight(R.color.cpp_string, start, end, offset)
            return true
        }

        if (afterTypename) {
            afterTypename = false
            typenames[lexem] = brackets
            highlight(R.color.colorAccent, start, end, offset)
            return true
        }
        return false
    }


    private fun highlight(color: Int, start: Int, end: Int, offset: Int) {
        code.setSpan(
            ForegroundColorSpan(resources.getColor(color)),
            start + offset, end + offset, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun handleCloseBracket() {
        typenames.forEach { (t, u) -> if (u == brackets) {typenames.remove(t)} }
    }

    private fun nextToken(position: Int, line: String): Pair<String, Int> {
        if (line[position] == '"') {
            return findString(position, line, '"')
        }
        if (position < line.length - 1 && line.substring(position, position + 2) == "//") {
            return Pair(LINE_COMMENT, position)
        }
        if (afterInclude && line[position] == '<') {
            return findString(position, line, '>')
        }
        when (line[position]) {
            '{' -> brackets++
            '}' -> {
                brackets--
                handleCloseBracket()
            }
            '#' -> preproc = true
        }
        if (line.substring(position).startsWith("\t") ||
            DELIMETERS.contains(line[position])
        ) {
            return Pair(NO_HIGHLIGHT, position + 1)
        }
        return findLexem(position, line)
    }

    private fun findString(position: Int, line: String, endBracket: Char): Pair<String, Int> {
        var nextPosition = position + 1
        while (nextPosition < line.length && line[nextPosition] != endBracket) {
            nextPosition++
        }
        return Pair(STRING, nextPosition + 1)
    }

    private fun findLexem(position: Int, line: String): Pair<String, Int> {
        var nextPosition = position + 1
        lastWordStart = position
        while (nextPosition < line.length && !DELIMETERS.contains(line[nextPosition])) {
            nextPosition++
        }
        lastWordEnd = nextPosition
        return Pair(line.substring(position, nextPosition), nextPosition)
    }

}
