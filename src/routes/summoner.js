const express = require('express')
const router = express.Router()
const https = require('https')
const app = require('../app')

router.get('/summoner/:name', (req, res) => {
    https.get(('https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/' + req.params.name + '?api_key=' + app.api), (resp) => {
        let data = '';
        resp.on('data', (chunk) => {
            data += chunk;
        });
        resp.on('end', () => {
            let json = JSON.parse(data)
            if (!json.hasOwnProperty('status')){
                https.get(('https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/' + json.puuid + '/ids?start=0&count=20&api_key=' + app.api), (resp2) => {
                    let data2 = '';
                    resp2.on('data', (chunk) => {
                        data2 += chunk;
                    });
                    resp2.on('end', () => {
                        json.history = []
                        for(var match in JSON.parse(data2)){
                            json.history.push(JSON.parse(data2)[match].toString())
                        }
                        console.log(json)
                        res.send(json);
                    });
                }).on("error", (err) => {
                    return "Error: " + err.message;
                });
            } else {
                json.history = ["404"]
                console.log(json)
                res.send(json);
            }
        });
    }).on("error", (err) => {
        res.send("Error: " + err.message);
    });
})

router.get('/maestry/:id', (req, res) => {
    https.get(('https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/' + req.params.id + '?api_key=' + app.api), (resp) => {
        let data = '';
        resp.on('data', (chunk) => {
            data += chunk;
        });
        resp.on('end', () => {
            json = []
            for(var maestry in JSON.parse(data)){
                json.push(JSON.parse(data)[maestry].toString())
            }
            res.send(data);
        });
    }).on("error", (err) => {
        return "Error: " + err.message;
    });
})

router.get('/elo/:id', (req, res) => {
    https.get(('https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/' + req.params.id + '?api_key=' + app.api), (resp) => {
        let data = '';
        resp.on('data', (chunk) => {
            data += chunk;
        });
        resp.on('end', () => {
            res.send(JSON.parse(data));
        });
    }).on("error", (err) => {
        return "Error: " + err.message;
    });
})

router.get('/match/:matchid', (req, res) => {
    https.get(('https://europe.api.riotgames.com/lol/match/v5/matches/' + req.params.matchid + '?api_key=' + app.api), (resp) => {
        let data = '';
        resp.on('data', (chunk) => {
            data += chunk;
        });
        resp.on('end', () => {
            console.log(JSON.parse(data))
            res.send(JSON.parse(data));
        });
    }).on("error", (err) => {
        return "Error: " + err.message;
    });
})

module.exports = router