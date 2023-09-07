package com.example.myapplication13

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication13.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    var datas:MutableList<String>? = null
    lateinit var adapter : MyAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val bgColor = sharedPreferences.getString("color", "")
        binding.rootLayout.setBackgroundColor(Color.parseColor(bgColor))

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(false)//전체 화면으로 설정
            val controller = window.insetsController
            if(controller != null){
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        /* Adapter 연결
        binding.mainRecycerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(datas)
        //binding.mainRecycerView.adapter = MyAdapter(datas)
        binding.mainRecycerView.adapter = adapter*/

        //액티비티 화면 되돌리기
        val requestLauncher:ActivityResultLauncher<Intent>
        = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val d3 = it.data!!.getStringExtra("result")?.let{
                datas?.add(it)
                adapter.notifyDataSetChanged()
            }
            //Log.d("mobileApp", d3!!)
        }

        //인텐트를 이용해 다른 컴포넌트 간접 호출 (AddActivitiy 호출)
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra("data1", "mobile") //데이터도 같이 전달
            intent.putExtra("data2", "app")

            //startActivityForResult(intent, 10) //결과값을 다시 전달받음
            requestLauncher.launch(intent)
        }
        /*
        //불필요한 선언없이 전달
        datas = savedInstanceState?.let{
            it.getStringArrayList("mydatas")?.toMutableList()
        }?:let{
            mutableListOf<String>()
        }
        */

        datas = mutableListOf<String>()
        //DB 읽어오기
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from todo_tb", null)
        while(cursor.moveToNext()){
            datas?.add(cursor.getString(1))
        }
        db.close()

        val items = arrayOf<String>("내장")
        binding.fileBtn.setOnClickListener {
            AlertDialog.Builder(this).run{
                setTitle("저장 위치 선택")
                setIcon(android.R.drawable.ic_dialog_info)
                setSingleChoiceItems(items, 1, object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if(p1 == 0){ //내장 메모리
                            // 저장
                            val file = File(filesDir, "test.txt")
                            val writeStream: OutputStreamWriter = file.writer()
                            writeStream.write("hello android")
                            writeStream.write("$items[p1]")
                            for(i in datas!!.indices)
                                writeStream.write(datas!![i])
                            writeStream.flush()

                            //읽어 오기
                            val readStream:BufferedReader = file.reader().buffered()
                            readStream.forEachLine {
                                Log.d("mobileApp", "$it")
                            }
                        }
                    }
                })
                setPositiveButton("선택", null)
                show()
            }
        }

        binding.mainRecycerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(datas)
        binding.mainRecycerView.adapter = adapter
        binding.mainRecycerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    //인텐트 생명주기
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putStringArrayList("mydatas", ArrayList(datas))
    }

    /* 결과값이 돌아올 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10 && resultCode == RESULT_OK){
            val d3 = data?.getStringExtra("text")
            Log.d("mobileApp", d3!!)
        }
    }*/



    override fun onResume() {
        super.onResume()
        val bgColor = sharedPreferences.getString("color", "")
        binding.rootLayout.setBackgroundColor(Color.parseColor(bgColor))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_main_setting){
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}