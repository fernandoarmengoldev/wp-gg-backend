# WP.GG Backend - League of Legends Statistics API

![Bun](https://img.shields.io/badge/Bun-000000?style=for-the-badge&logo=bun&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

WP.GG Backend is a Bun + TypeScript REST API server that serves as the backend for the WP.GG League of Legends statistics application. It integrates with Riot Games API to fetch real-time summoner data and stores match statistics in MongoDB for analytics and champion statistics calculation.

Most of the original development of this backend server (research, coding, testing, design, implementation, and deployment) was carried out during 2021 and 2022. 

After that period, the project remained inactive for some time. In 2026, development was resumed to continue improving the codebase, adapt the server to the Riot Games API changes introduced over the years, improve performance, and keep adding new features.

## Main Features

### Summoner Data
- Search summoners by name on EUW server
- Retrieve summoner profile (ID, PUUID, profile icon, level)
- Get recent match history (last 20 matches)
- Fetch champion mastery data for summoners
- Get ranked information (Solo/Duo and Flex queue ranks)

### Match Statistics
- Retrieve complete match data from Riot API
- Store matches in MongoDB for analysis
- Track individual player performance in matches
- Access detailed participant information (items, runes, spells, KDA, etc.)

### Champion Analytics
- **Win Rate & Pick Rate**: Calculate win and pick rates for each champion from stored matches
- **Matchups**: Determine best and worst matchups based on lane opponents
- **Recommended Build**:
  - Most popular boots
  - Most popular mythic items
  - Most popular legendary items
- **Spells**: Most used summoner spells
- **Runes**: Most popular rune configurations
- **Position Detection**: Automatically determine champion roles (Top, Jungle, Mid, Bot, Support)

### Data Collection
- Automatic match collection system (configurable)
- Random match sampling from existing database
- Recursive match discovery through participant PUUIDs
- Periodic data updates via interval function

## Technologies Used

- **Runtime**: Bun
- **Language**: TypeScript
- **Server**: `Bun.serve()` for the REST API
- **Database**: MongoDB for match storage and analytics
- **API Integration**: Riot Games API for real-time LoL data
- **Environment**: dotenv for configuration management
- **Development**: Bun watch mode for hot reloading

## Project Structure

```
.env                      # Environment variables for local development
tsconfig.json             # TypeScript configuration
src/
├── app.ts                  # Main application entry point
├── types.ts                # Shared TypeScript types
├── routes/
│   ├── summoner.ts         # Summoner-related API endpoints
│   └── champion.ts         # Champion statistics endpoints
├── scripts/
│   └── data-generator.ts   # Automatic match collection script
└── ...
```

## API Endpoints

### Summoner Endpoints
- `GET /api/summoner/:name` - Get summoner data and match history
- `GET /api/maestry/:id` - Get champion mastery data for a summoner
- `GET /api/elo/:id` - Get ranked information for a summoner
- `GET /api/match/:matchid` - Get detailed match data

### Champion Statistics Endpoints
- `GET /api/matchesFromChamp/:champ` - Get comprehensive champion statistics
  - Win rate and pick rate
  - Best and worst matchups
  - Recommended items (boots, mythics, legendaries)
  - Recommended spells and runes
- `GET /api/position` - Get all champion positions
- `GET /api/positionSetting` - Recalculate champion positions from match data

## Data Sources

The server integrates with:

1. **Riot Games API** (official LoL data)
   - Summoner API: `https://euw1.api.riotgames.com/lol/summoner/v4/`
   - Match API: `https://europe.api.riotgames.com/lol/match/v5/`
   - Champion Mastery API: `https://euw1.api.riotgames.com/lol/champion-mastery/v4/`
   - League API: `https://euw1.api.riotgames.com/lol/league/v4/`

2. **MongoDB**
   - Collection: `matches` - Stored match data
   - Collection: `positions` - Champion role classifications

## Setup Instructions

### Prerequisites
- Bun installed
- MongoDB running locally or accessible
- Riot Games API key (get from [developer.riotgames.com](https://developer.riotgames.com/))

### Installation

1. Clone the repository
2. Install dependencies:
   ```bash
   bun install
   ```

3. Create a `.env` file in the project root (you can copy `.env.example`):
    ```
    DB_URL=mongodb://0.0.0.0:27017
    RIOT_API_KEY=your_riot_api_key_here
    GET_RANDOM_MATCHES=false
    ```

4. Start MongoDB service

5. Run the server:
    ```bash
# Development mode with hot reload
bun run dev

# Production mode
bun run start

# Type-check the project
bun run typecheck
     ```

The server will run on port 3000 by default.

## Configuration

### Environment Variables

- `DB_URL` - MongoDB connection string
- `RIOT_API_KEY` - Your Riot Games API key
- `GET_RANDOM_MATCHES` - Enable/disable automatic match collection (`true`/`false`)

### Automatic Data Collection

When `GET_RANDOM_MATCHES` is set to `true`, the server runs a background interval function that:
1. Randomly selects a match from the database
2. Picks a random participant from that match
3. Fetches their recent match history
4. Stores new matches in MongoDB

This helps continuously grow the match database for more accurate statistics.

## Dependencies

```json
{
  "dependencies": {
    "dotenv": "^17.0.0",
    "mongodb": "^6.0.0"
  },
  "devDependencies": {
    "@types/bun": "^1.3.0",
    "typescript": "^5.9.0"
  }
}
```

## Features & Algorithms

### Matchup Calculation
The algorithm calculates matchups by:
1. Identifying lane opponents (positions 0 vs 5, 1 vs 6, etc.)
2. Tracking wins and losses against each champion
3. Filtering for significant matchups (win rate ≥70% or ≤30%)
4. Ranking by performance and returning top/bottom matchups

### Position Detection
Champion positions are determined by:
1. Analyzing all matches where the champion was played
2. Counting occurrences in each position (0-4 = team positions)
3. Calculating percentage distribution
4. Assigning positions that cumulatively reach 65% or more

### Item Classification
Items are categorized into:
- **Boots**: 7 types (Mercury's, Berserker's, etc.)
- **Mythics**: 26 items (historical mythic items)
- **Legendaries**: 62+ core items

## Future Improvements

- Add rate limiting to avoid Riot API restrictions
- Implement caching strategies for frequently accessed data
- Add support for multiple regions
- Implement more sophisticated statistics algorithms
- Add real-time match tracking
- Create web dashboard for monitoring
- Add authentication and API key management
- Implement data validation and error handling improvements

## License

This project is an educational/fan application of League of Legends. All data is property of Riot Games and is used in accordance with their terms of service and API policies.

## Credits

- Data provided by Riot Games API
- MongoDB for database management
- Bun and MongoDB for the backend runtime and storage
- Inspired by op.gg and other LoL statistics platforms

---

Developed with ❤️ for the League of Legends community
