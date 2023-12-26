# Pokédex + Team Builder

## Description
This project is a one-stop hub for every Pokémon fan. It has three main functions - a Pokédex and team builder feature, an ultimate quiz challenge to show off your Pokémon knowledge, and a news center for updated Pokémon information. 

## Features
- PokéDex 
    - Detailed information for all 1025 Pokémon, including stats, abilities, and moves.
    - Search functionality to filter Pokémon by name.
    - Integration with Redis for saving your Pokémon team.
- Ultimate Trainer Challenge
    - Earn badges on your trainer profile by playing the quiz challenge.
- Pokémon News Center
    - NewsAPI integration for updated Pokémon news.
- (Planned) Unit tests to ensure the reliability and functionality of the application.

## Installation

To start:

### Prerequisites
- Docker

### Clone the repository
```bash
git clone https://github.com/username/pokemon-springboot.git
cd pokemon-springboot
```

### Installation with Docker
Build the Docker image
```bash
docker build -t pokemon-springboot .
```


### Run the container
```bash
docker run -d -p 8080:8080 pokemon-springboot
```

### Frameworks and Dependencies
- PokeAPI: https://pokeapi.co/
- Pokemon Trivia API: https://github.com/PokeDis/PokemonTrivia
- NewsAPI: https://newsapi.org/ 
- Tailwind CSS
- Spring Boot
- Thymeleaf
- Redis
- JUnit