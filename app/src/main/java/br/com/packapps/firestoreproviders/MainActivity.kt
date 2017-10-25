package br.com.packapps.firestoreproviders

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap
import android.support.annotation.NonNull
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


class MainActivity : AppCompatActivity() {


    var db : FirebaseFirestore? = null
    var myIdDocumento: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //step one : instance our FirebaseFirestore
        db = FirebaseFirestore.getInstance()

        //step two: write our data
        var provider : HashMap<String, Any> = hashMapOf(
                "provider_id" to 1,
                "color" to "black",
                "lat" to -1.54321,
                "long" to -48.54321,
                "location_id" to 2,
                "type" to 18,
                "category" to 18)

        //step two: write our data
        db!!.collection("providers")
                .add(provider)
                .addOnSuccessListener {
                    documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                    myIdDocumento = documentReference.id
                }
                .addOnFailureListener {
                    e -> Log.w("TAG", "Error adding document", e)
                }




        //## Action buttom
        fab.setOnClickListener { view ->

            var providerUpdate : HashMap<String, Any> = hashMapOf(
                    "provider_id" to 1,
                    "color" to "black",
                    "lat" to -1.98765, //changed
                    "long" to -48.98765, //changed
                    "location_id" to 2,
                    "type" to 18,
                    "category" to 18)

            //Step tree: update data
            if (!myIdDocumento.isEmpty()){
                db!!.collection("providers").document(myIdDocumento)
                        .update(providerUpdate)
                        .addOnCompleteListener {
                            documentReference ->
                            Log.i("TAG", "update successful:" + documentReference.isSuccessful)
                            Snackbar.make(view, "update successful:" + documentReference.isSuccessful, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        }
                        .addOnFailureListener {
                            e -> Log.e("TAG", "failure: "+ e.message)
                        }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {

            //Step four: getting data
            db!!.collection("providers")
                    .get()
                    .addOnCompleteListener(OnCompleteListener {
                        task ->
                        Log.i("TAG", "get successful: " + task.isSuccessful)
                        if (task.isSuccessful){
                            for (document : DocumentSnapshot in task.result){
                                Log.i("TAG", "document data: " + document.data)
                            }

                        }else{
                            Log.e("TAG", "get error: " + task.exception)
                        }
                    })

            true
        } else super.onOptionsItemSelected(item)

    }
}
