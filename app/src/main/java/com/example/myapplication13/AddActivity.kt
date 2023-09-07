package com.example.myapplication13

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import com.example.myapplication13.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddBinding //다른 함수에서도 사용할 수 있게 전역변수, 초기화는 나중에
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val d1 = intent.getStringExtra("data1") //데이터를 전달받음
        val d2 = intent.getStringExtra("data2")

        binding.tv.text = (d1+d2)

        /* //결과값을 다시 전달
        binding.button1.setOnClickListener{
            intent.putExtra("test", "world")
            setResult(RESULT_OK, intent)
            finish()
        }*/

        binding.button5.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        //intent filter - AndroidManiFest <Activity> 작성
        binding.button2.setOnClickListener{
            val intent = Intent()
            intent.action = "ACTION_EDIT"
            intent.data = Uri.parse("http://www.google.com")
            startActivity(intent)
        }
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.button3.setOnClickListener{
            binding.addEditView.requestFocus()
            manager.showSoftInput(binding.addEditView, InputMethodManager.SHOW_IMPLICIT)
        }
        binding.button4.setOnClickListener{
            manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }


    //옵션 메뉴가 선택될 때 (button은 주석처리)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add_save){
            val inputData = binding.addEditView.text.toString()
            //DB에 저장하기
            val db = DBHelper(this).writableDatabase
            db.execSQL("insert into todo_tb (todo) values (?)", arrayOf<String>(inputData))
            db.close()

            //사용자의 입력을 추가
            intent.putExtra("result", inputData)
            setResult(RESULT_OK, intent)
            finish()
            return true
        }
        return false
    }
}