package com.example.myapplication

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.RuntimeException
import java.lang.StringBuilder
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val defaultString: String = "Print an expression"
    private var lastResult : Double = 0.0;
    private var hasResult : Boolean = false;
    private var mem : Double = Double.NaN

    private lateinit var exprString: StringBuilder

    lateinit var expression: TextView
    lateinit var res: TextView

    lateinit var calculate: Button
    lateinit var memory: Button
    lateinit var backspace: Button

    lateinit var symbols: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val x = resources.configuration.orientation
        if (x==1) {
            setContentView(R.layout.activity_main)
        } else if (x==2) {
            setContentView(R.layout.activity_landscape)
        }

        expression = findViewById(R.id.expression)
        res = findViewById(R.id.result)
        memory = findViewById(R.id.mem)
        backspace = findViewById(R.id.backspace)
        calculate = findViewById(R.id.calc)

        symbols = arrayOf(
            findViewById(R.id.zero),
            findViewById(R.id.one),
            findViewById(R.id.two),
            findViewById(R.id.three),
            findViewById(R.id.four),
            findViewById(R.id.five),
            findViewById(R.id.six),
            findViewById(R.id.seven),
            findViewById(R.id.eight),
            findViewById(R.id.nine),
            findViewById(R.id.point),
            findViewById(R.id.leftbracket),
            findViewById(R.id.rightbracket),
            findViewById(R.id.plus),
            findViewById(R.id.minus),
            findViewById(R.id.mul),
            findViewById(R.id.div)
        )
        for (symbol in symbols) {
            symbol.setOnClickListener() {
                exprString.append(symbol.text)
                hasResult = false
                updateString()
            }
        }

        backspace.setOnClickListener() {
            hasResult = false
            if (exprString.isNotEmpty()) {
                exprString.deleteCharAt(exprString.length - 1)
                updateString()
            }
        }
        backspace.setOnLongClickListener() {
            hasResult = false
            exprString = StringBuilder()
            updateString()
            true
        }
        memory.setOnClickListener() {
            if (hasResult) {
                mem = lastResult
            }
            else {
                if (!mem.isNaN()) {
                    exprString.append(format(mem))
                    updateString()
                }
            }
        }
        calculate.setOnClickListener() {
            try {
                lastResult = Parser(exprString.toString()).evaluate()
                hasResult = true
                updateString()

            } catch (e: RuntimeException) {
                res.text = e.message
            }
        }

        exprString = StringBuilder()
    }

    private fun format(x : Double) : String {
        val rest = x - x.roundToInt()
        val res : String
        if (rest == 0.0) {
            res = x.roundToInt().toString()
        }
        else {
            res = x.toString()
        }
        return res
    }

    private fun updateString() {
        if (exprString.isEmpty()) {
            expression.text = defaultString
            expression.setTextIsSelectable(false)
        } else {
            expression.text = exprString.toString()
            expression.setTextIsSelectable(true)
        }
        if (hasResult) {
            res.text = format(lastResult)
            res.setTextIsSelectable(true)
        }
        else {
            res.setTextIsSelectable(false)
            res.text = ""
        }
    }


    override fun onStart() {
        super.onStart()
        updateString()
    }

    override fun onRestart() {
        super.onRestart()
        updateString()
    }

    override fun onResume() {
        super.onResume()
        updateString()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("Expression", exprString.toString())
        outState.putDouble("Memory", mem)
        outState.putBoolean("HasResult", hasResult)
        outState.putDouble("LastResult", lastResult)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val text = savedInstanceState.getString("Expression")
        mem = savedInstanceState.getDouble("Memory")
        hasResult = savedInstanceState.getBoolean("HasResult")
        lastResult = savedInstanceState.getDouble("LastResult")
        if (text != null) {
            exprString = StringBuilder(text)
        }
    }
}