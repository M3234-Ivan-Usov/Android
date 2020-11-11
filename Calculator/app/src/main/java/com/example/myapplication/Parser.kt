package com.example.myapplication

import java.lang.RuntimeException

class Parser(string: String) {
    private var isNumber: Boolean = false
    private val delims: String = "+-*/=()"
    private val digits: String = "0123456789."
    private var expression: String = string
    private var position: Int = 0
    private var token: String = ""

    private fun getToken() {
        isNumber = false
        token = ""
        if (position == expression.length) {
            token = "End"
            return
        }
        if (isDelim()) {
            token += expression[position]
            position++
            isNumber = false
        } else if (isDigit()) {
            isNumber = true
            while (!isDelim()) {
                token += expression[position]
                position++
                if (position == expression.length) {
                    break
                }
            }
        } else {
            token = "End"
        }
    }

    private fun isDelim(): Boolean {
        return delims.contains(expression[position])
    }

    private fun isDigit(): Boolean {
        return digits.contains(expression[position])
    }

    private fun addition(): Double {
        var res: Double = multiplication()
        var op: Char = token[0]
        while (op == '+' || op == '-') {
            getToken()
            val part: Double = multiplication()
            if (op == '+') {
                res += part
            } else {
                res -= part
            }
            op = token[0]
        }
        return res
    }

    private fun multiplication(): Double {
        var res: Double = unary()
        var op: Char = token[0]
        while (op == '*' || op == '/') {
            getToken()
            val part: Double = multiplication()
            if (op == '*') {
                res *= part
            } else {
                if (part == 0.0) {
                    throw RuntimeException("Division by zero")
                }
                res /= part
            }
            op = token[0]
        }
        return res
    }

    private fun unary(): Double {
        var sign: String = ""
        if (!isNumber && (token == "+" || token == "-")) {
            sign = token
            getToken()
        }
        var res: Double = brackets()
        if (sign == "-") {
            res = -res
        }
        return res
    }

    private fun brackets(): Double {
        val res: Double
        if (token == "(") {
            getToken()
            res = addition()
            if (token != ")") {
                throw RuntimeException("Brackets Mismatch")
            }
            getToken()
        } else {
            res = parseNumber()
        }
        return res
    }

    private fun parseNumber(): Double {
        if (isNumber) {
            val res = token.toDoubleOrNull()
            if (res != null) {
                getToken()
                return res
            }
        }
        throw RuntimeException("Syntax Error")
    }

    fun evaluate(): Double {
        if (expression.isEmpty()) {
            throw RuntimeException("Empty Expression")
        }
        getToken()
        val res: Double = addition()
        println("result is $res")
        if (token != "End") {
            throw RuntimeException("Syntax Error")
        }
        return res
    }

}