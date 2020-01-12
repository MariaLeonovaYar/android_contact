package ru.tensor.sbis.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.tensor.sbis.recyclerview.items.FruitListItem
import ru.tensor.sbis.recyclerview.items.Fruits
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), FruitsAdapter.ClickHandler {

    private var toolbar: Toolbar? = null
    private var rootView: View? = null
    private var fruitsRecyclerView: RecyclerView? = null
    private var fruitsAdapter: FruitsAdapter? = null
    private var searchQuery: String? = null
    var contacts: TextView? = null
    private var idDelete: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fruitsAdapter = FruitsAdapter(this)
        rootView = findViewById(R.id.root_view)
        toolbar = findViewById(R.id.fruits_toolbar)
        setSupportActionBar(toolbar!!)
        fruitsRecyclerView = findViewById<RecyclerView>(R.id.fruit_recycler_view).apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = fruitsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(getDrawable(R.drawable.fruits_list_divider)!!)
                }
            )
        }

        findViewById<View>(R.id.add_item_btn).setOnClickListener {
            getContacts()
        }
        findViewById<View>(R.id.remove_item_btn).setOnClickListener {
            fruitsAdapter!!.removeFirst()
        }
        findViewById<View>(R.id.reset_list_btn).setOnClickListener {
           resetFruits()
        }
    }

    private fun resetFruits() {
        val items: List<FruitListItem> =
            if (searchQuery != null) {
                Fruits.items.filter { it.name.contains(searchQuery!!, true) }
            } else {
                Fruits.items
            }
        fruitsAdapter!!.setItemsWithDiff(items)
        fruitsRecyclerView!!.scrollToPosition(0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.fruit_toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.fruits_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText
                resetFruits()
                return true
            }
        })
        return true
    }

    //когда кликнули на элемент
    override fun onFruitItemClick(item: FruitListItem) {
        Snackbar.make(rootView!!, item.name + " " + item.quantity, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        toolbar = null
        fruitsAdapter = null
        rootView = null
        fruitsRecyclerView = null
        super.onDestroy()
    }

    //Метод для получения контактов из телефонной книги
    fun getContacts() {

        var phoneNumber: String? = null

        //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
        val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI
        val _ID = ContactsContract.Contacts._ID
        val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
        val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER

        val PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER


        val output = StringBuffer()
        val contentResolver = contentResolver
        val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)

        //Запускаем цикл обработчик для каждого контакта:
        if (cursor!!.count > 0) {

            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext()) {
                val contact_id = cursor.getString(cursor.getColumnIndex(_ID))
                val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                val hasPhoneNumber =
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)))

                //Получаем имя:
                if (hasPhoneNumber > 0) {
                    output.append("\n Имя: $name")
                    val phoneCursor = contentResolver.query(
                        PhoneCONTENT_URI, null,
                        "$Phone_CONTACT_ID = ?", arrayOf(contact_id), null
                    )

                    //и соответствующий ему номер:
                    while (phoneCursor!!.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER))
                        output.append("\n Телефон: " + phoneNumber!!)
                    }

                    //вычисляем рандомную дату
                    val sdf = SimpleDateFormat("dd/MM")
                    val currentDate = sdf.format(Date().time*(0..500).random() - Date().time)

                    //кидаем все наши данные в список и выводим
                    fruitsAdapter!!.addItemToFirst(
                        items.newFruitListItem
                            (name, currentDate, phoneNumber.toString(),"https://im0-tub-ru.yandex.net/i?id=9f27e565c9b43adff5987b2c7ad21c12&n=13")
                    )
                }
            }
        }
    }
}

