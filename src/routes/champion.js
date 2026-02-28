const express = require('express')
const router = express.Router()
const app = require('../app')

router.get('/matchesFromChamp/:champ', async (req, res) => {
    let db = app.getDatabase()

    let collection = db.collection("matches")

    let cursor = await collection.find({ "info.participants.championId": parseInt(req.params.champ), "info.gameMode": "CLASSIC" })
    let matches = await cursor.toArray()

    //IDs de items
    let boots = [3009, 3117, 3158, 3006, 3047, 3020, 3111]
    let mythics = [4644, 6632, 6691, 6692, 3001, 6656, 6662, 6671, 6630, 3152, 6673, 4005, 6672, 6653, 3190, 6655, 6617, 4636, 6693, 4633, 2065, 6631, 3068, 3078, 6664]
    let legendaries = [3031, 3109, 3100, 3036, 3004, 3156, 3041, 3139, 3222, 8020, 8001, 3003, 3504, 6696, 3102, 3071, 3153, 3072, 6609, 3011, 4629, 3742, 6333, 4637, 3814, 3508, 3121, 4401, 3110, 3193, 3026, 3124, 4628, 3181, 3165, 3033, 3042, 3115, 6675, 3046, 3089, 3143, 3094, 3074, 3107, 3085, 3116, 3040, 6695, 6694, 4645, 6035, 3065, 6616, 3053, 6676, 3075, 3748, 3179, 3135, 3083, 3119, 3091, 3142, 3050, 3157]

    let hashMapBoots = {}
    let hashMapMythics = {}
    let hashMapOther = {}

    let hashMapSpells = {}
    let hashMapRunes = {}

    let goodAgainst = {}
    let totalEachChamp = {}

    let win = 0

    matches.forEach(element => {
        let index = 0

        element.info.participants.forEach(participant => {
            if (participant.championId == parseInt(req.params.champ)) {

                //Calculo de enemigo en linea
                let enemy = index
                if (enemy < 5) { enemy += 5 } else { enemy -= 5 }

                try {
                    //Winrate y counter calculo
                    if (participant.win == true) {
                        win++
                        if (element.info.participants[enemy].championId in goodAgainst) {
                            goodAgainst[element.info.participants[enemy].championId] = goodAgainst[element.info.participants[enemy].championId] + 1;
                        } else {
                            goodAgainst[element.info.participants[enemy].championId] = 1;
                        }
                    } else {
                        if (element.info.participants[enemy].championId in goodAgainst) {
                            goodAgainst[element.info.participants[enemy].championId] = goodAgainst[element.info.participants[enemy].championId] - 1;
                        } else {
                            goodAgainst[element.info.participants[enemy].championId] = -1;
                        }
                    }
                    if (element.info.participants[enemy].championId in totalEachChamp) {
                        totalEachChamp[element.info.participants[enemy].championId] = totalEachChamp[element.info.participants[enemy].championId] + 1;
                    } else {
                        totalEachChamp[element.info.participants[enemy].championId] = 1;
                    }
                } catch (error) {
                    console.error(error, enemy)
                }

                //Items calculo
                let items = [participant.item0, participant.item1, participant.item2, participant.item3, participant.item4, participant.item5]
                items.forEach(item => {
                    //Filtro para botas, mitico u otros
                    if (boots.includes(item)) {
                        if (item in hashMapBoots) {
                            hashMapBoots[item] = hashMapBoots[item] + 1;
                        } else {
                            hashMapBoots[item] = 1;
                        }
                    } else if (mythics.includes(item)) {
                        if (item in hashMapMythics) {
                            hashMapMythics[item] = hashMapMythics[item] + 1;
                        } else {
                            hashMapMythics[item] = 1;
                        }
                    } else if (legendaries.includes(item)) {
                        if (item in hashMapOther) {
                            hashMapOther[item] = hashMapOther[item] + 1;
                        } else {
                            hashMapOther[item] = 1;
                        }
                    }
                });

                //Spells calculo
                let spells = [participant.summoner1Id, participant.summoner2Id]
                spells.forEach(spell => {
                    if (spell in hashMapSpells) {
                        hashMapSpells[spell] = hashMapSpells[spell] + 1;
                    } else {
                        hashMapSpells[spell] = 1;
                    }
                });

                //Rune calcule
                let runes = []
                Object.entries(participant.perks.statPerks).forEach(statPerk => {
                    runes.push(statPerk)
                });
                participant.perks.styles.forEach(style => {
                    runes.push(style.style)
                    style.selections.forEach(selection => {
                        runes.push(selection.perk)
                    });
                });
                if (runes in hashMapRunes) {
                    hashMapRunes[runes] = hashMapRunes[runes] + 1;
                } else {
                    hashMapRunes[runes] = 1;
                }

                return
            }
            index++
        });
    });



    var json = new Object();

    if (matches.length >= 1) {
        json.winRate = (win / matches.length * 100).toFixed(2);
        json.pickRate = (matches.length / await collection.count() * 100).toFixed(2);
    
        //JSON counter
        for (var key in goodAgainst) {
            let num = (totalEachChamp[key] + goodAgainst[key]) / (totalEachChamp[key] * 2) * 100
            if (num >= 70 || num <= 30) {
                delete goodAgainst[key]
                key--
            }
        }
        let bestFive = getFirstNElements(4, goodAgainst)
        let worstFive = getLastNElements(4, goodAgainst)
        json.bestFive = []
        for (var key in bestFive) {
            json.bestFive.push({
                id: bestFive[key].name,
                value: ((totalEachChamp[bestFive[key].name] + bestFive[key].value) / (totalEachChamp[bestFive[key].name] * 2) * 100).toFixed(2)
            })
        }
        json.worstFive = []
        for (var key in worstFive) {
            json.worstFive.push({
                id: worstFive[key].name,
                value: ((totalEachChamp[worstFive[key].name] + worstFive[key].value) / (totalEachChamp[worstFive[key].name] * 2) * 100).toFixed(2)
            })
        }
        //Algoritmo mas preciso cuando hay mas partidas pero menos al haber pocas
        // let finalWR = []
        // for (var key in goodAgainst) {
        //     finalWR.push({ name: key, value: (totalEachChamp[key] + goodAgainst[key]) / (totalEachChamp[key] * 2) * 100 })
        // }
        // console.log(finalWR.sort())
    
        //JSON otherItem
        const finalBoots = getFirstNElements(2, hashMapBoots)
        json.boots = [
            { value: finalBoots[0].name, porcentaje: (finalBoots[0].value / matches.length * 100).toFixed(2) },
            { value: finalBoots[1].name, porcentaje: (finalBoots[1].value / matches.length * 100).toFixed(2) }
        ]
    
        //JSON otherItem
        const finalMythics = getFirstNElements(2, hashMapMythics)
        json.mythics = [
            { value: finalMythics[0].name, porcentaje: (finalMythics[0].value / matches.length * 100).toFixed(2) },
            { value: finalMythics[1].name, porcentaje: (finalMythics[1].value / matches.length * 100).toFixed(2) }
        ]
    
        //JSON otherItem
        const finalOtherItem = getFirstNElements(3, hashMapOther)
        json.otherItem = [
            { value: finalOtherItem[0].name, porcentaje: (finalOtherItem[0].value / matches.length * 100).toFixed(2) },
            { value: finalOtherItem[1].name, porcentaje: (finalOtherItem[1].value / matches.length * 100).toFixed(2) },
            { value: finalOtherItem[2].name, porcentaje: (finalOtherItem[2].value / matches.length * 100).toFixed(2) }
        ]
    
        //JSON Spells
        const finalSpells = getFirstNElements(3, hashMapSpells)
        json.spells = [
            { value: finalSpells[0].name, porcentaje: (finalSpells[0].value / matches.length * 100).toFixed(2) },
            { value: finalSpells[1].name, porcentaje: (finalSpells[1].value / matches.length * 100).toFixed(2) },
            { value: finalSpells[2].name, porcentaje: (finalSpells[2].value / matches.length * 100).toFixed(2) },
        ]
    
        //JSON Runas
        const finalRunes = getFirstNElements(2, hashMapRunes)
        json.runes = [
            { value: finalRunes[0].name, porcentaje: (finalRunes[0].value / matches.length * 100).toFixed(2) },
            { value: finalRunes[1].name, porcentaje: (finalRunes[1].value / matches.length * 100).toFixed(2) }
        ]
    } else {
        json.bestFive = []
    }

    console.log(json)
    //res.send(JSON.stringify(json))

    res.json(json)
})

router.get('/position', async (req, res) => {
    let db = app.getDatabase()

    let collection = db.collection("positions")
    let cursor = await collection.find().project({ "_id": 0, "value": 1, "positions": 1 });
    let matches = await cursor.toArray()

    console.log(matches)
    res.json(matches);
})

router.get('/positionSetting', async (req, res) => {
    let db = app.getDatabase()
    
    let collectionPositions = db.collection("positions")
    collectionPositions.deleteMany({})

    let collectionMatches = db.collection("matches")
    let cursor = await collectionMatches.find({ "info.gameMode": "CLASSIC" })
    let matches = await cursor.toArray()

    let champions = []

    var json = []

    matches.forEach(match => {
        match.info.participants.forEach(participant => {
            if (champions.indexOf(participant.championId) === -1) champions.push(participant.championId)
        })
    })

    champions.forEach(champ => {
        if (champ > 1000) {
            return
        }

        let positions = {
            top: 0,
            jg: 0,
            mid: 0,
            bot: 0,
            supp: 0
        }

        matches.forEach(match => {
            let index = 0
            match.info.participants.forEach(participant => {
                if (participant.championId == champ) {
                    switch (index) {
                        case 0, 5:
                            positions.top++
                            break;
                        case 1, 6:
                            positions.jg++
                            break;
                        case 2, 7:
                            positions.mid++
                            break;
                        case 3, 8:
                            positions.bot++
                            break;
                        case 4, 9:
                            positions.supp++
                            break;
                        default:
                            break;
                    }
                    return
                }
                index++
            });
        });

        let total = positions.top + positions.jg + positions.mid + positions.bot + positions.supp
        let porcentajes = getFirstNElements(5, positions)

        posicionesFinales = []
        tempPorcentaje = 0
        porcentajes.forEach(porcentaje => {
            if (tempPorcentaje < 65){
                tempPorcentaje += (porcentaje.value / total * 100)
                posicionesFinales.push(porcentaje.name)
            } else {
                return
            }
        });

        collectionPositions.insertOne({ value: champ, positions: posicionesFinales })
    });

    res.json("COMPLETADO");
})

function getFirstNElements(numElem, hashMap) {
    var array = [];
    for (var key in hashMap) {
        array.push({
            name: key,
            value: hashMap[key]
        });
    }
    return array.sort(function (a, b) {
        return (a.value > b.value) ? -1 : ((b.value > a.value) ? 1 : 0)
    }).slice(0, numElem)
}

function getLastNElements(numElem, hashMap) {
    var array = [];
    for (var key in hashMap) {
        array.push({
            name: key,
            value: hashMap[key]
        });
    }
    return array.sort(function (a, b) {
        return (a.value > b.value) ? 1 : ((b.value > a.value) ? -1 : 0)
    }).slice(0, numElem)
}

module.exports = router