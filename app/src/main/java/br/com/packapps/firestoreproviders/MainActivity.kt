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
import br.com.packapps.firestoreproviders.models.Provider
import br.com.packapps.firestoreproviders.util.DistanceCalculator
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
//        var provider : HashMap<String, Any> = hashMapOf(
//                "provider_id" to 1,
//                "color" to "black",
//                "lat" to -1.54321,
//                "long" to -48.54321,
//                "location_id" to 2,
//                "type" to 18,
//                "category" to 18)

        val provider : Provider = Provider(1, "white", -1.4580218, -48.4968418, 18, 23)
        //patio belem : -1.4580218, -48.4968418
        //grao pará : -1.3904519, -48.4673761

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

            var providerUpdate : Provider = provider
            providerUpdate.lat = -1.54321 //changed
            providerUpdate.long = -48.54321 //changed

            //convert to HashMap
            var providerUpdateMap : HashMap<String, Any> = hashMapOf(
                    "lat" to providerUpdate.lat,
                    "long" to providerUpdate.long
                    )



            //Step tree: update data
            if (!myIdDocumento.isEmpty()){
                db!!.collection("providers").document(myIdDocumento)
                        .update(providerUpdateMap)
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
                                val providerOb : Provider = document.toObject(Provider::class.java)
                                Log.i("TAG", "providerId: " + providerOb.providerId)
                                Log.i("TAG", "lat: " + providerOb.lat)

                                //### Calculate distance
                                val distance : Double = DistanceCalculator.distance(providerOb.lat, providerOb.long, -1.4572637, -48.501473, "K")
                                Log.i("TAG", "distance to me: " + distance + " km")
                            }

                        }else{
                            Log.e("TAG", "get error: " + task.exception)
                        }
                    })

            true
        } else super.onOptionsItemSelected(item)

    }
}
