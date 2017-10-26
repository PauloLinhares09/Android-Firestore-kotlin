package br.com.packapps.firestoreproviders.models

/**
 * Created by paulolinhares on 26/10/17.
 */

data class Provider(var providerId : Int = 0, var color : String = "", var lat : Double = 0.0, var long : Double = 0.0, var type : Int = 0, var category : Int = 0)