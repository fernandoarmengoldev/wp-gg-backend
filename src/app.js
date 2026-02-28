const express = require('express')
const summoner = require('./routes/summoner')
const matches = require('./routes/champion')
const data_generator = require('./scripts/data-generator')
const MongoClient = require("mongodb").MongoClient

const app = express()
require('dotenv').config({path: 'src/variables.env'})

//API Riot
const api = process.env.RIOT_API_KEY
exports.api = api

const getRandomMatches = process.env.GET_RANDOM_MATCHES

//Base de datos MongoDB
let database;
const url = process.env.DB_URL
MongoClient.connect(url, (err, db) => {
    if (err) {
        console.log("No se puede conectar a la base de datos")
        console.log(err)
    } else {
        console.log("Conectado a la base de datos")
        database = db.db("lol")
        //Funcion de busqueda y guardado de partidas
        if (getRandomMatches==true){
            setInterval(data_generator.intervalFunc, 5000);
        }
    }
})
function getDatabase() {
    return database
}
exports.getDatabase = getDatabase

//API Rest configuracion
app.set('port', process.env.PORT || 3000)
app.get('/', (req, res) => {
    res.json({ message: 'Servidor Activo' })
})

//Rutas de API
app.use('/api', summoner)
app.use('/api', matches)

app.listen(app.get('port'))
console.log('Server on port', app.get('port'))