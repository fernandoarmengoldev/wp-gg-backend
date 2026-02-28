const app = require('../app')
const https = require('https')

//Buscar partidas para guardar
async function intervalFunc() {
    let db = app.getDatabase()

    let collection = db.collection("matches")

    let match = await collection.aggregate([{ $sample: { size: 1 } }]).toArray()

    let puuid = match[0].metadata.participants[Math.floor(Math.random() * 10)]

    https.get(('https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/' + puuid + '/ids?start=0&count=20&api_key=' + app.api), (resp) => {
        let data = '';
        resp.on('data', (chunk) => {
            data += chunk;
        });
        resp.on('end', async () => {
            let matchid = JSON.parse(data)[Math.floor(Math.random() * 20)]
            https.get(('https://europe.api.riotgames.com/lol/match/v5/matches/' + matchid + '?api_key=' + app.api), (resp) => {
                let randomMatch = '';
                resp.on('data', (chunk) => {
                    randomMatch += chunk;
                });
                resp.on('end', () => {
                    let json = JSON.parse(randomMatch)
                    if (!json.hasOwnProperty('status')){
                        collection.insertOne(json)
                    }
                });
            }).on("error", (err) => {
                return "Error: " + err.message;
            });
        });
    }).on("error", (err) => {
        return "Error: " + err.message;
    });
}

exports.intervalFunc = intervalFunc