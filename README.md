# Pokédex + Team Builder

## Description
This project is a Pokédex web application. It allows users to browse through Pokémon and their stats, abilities, and movesets. The search functionality allows you to search by Pokemon or move. 

## Features
- PokeDex features for all 1025 Pokémon.
- Detailed information for each Pokémon, including stats, abilities, and moves.
- Search functionality to filter Pokémon by name.
- Integration with Redis for saving your Pokémon team.
- NewsAPI integration for updated Pokémon news
- (Planned) Daily quiz feature and progress tracker

## Installation

To start:

### Prerequisites
- Docker

### Clone the repository
```bash
git clone https://github.com/your-username/pokemon-project.git
cd pokemon-project
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