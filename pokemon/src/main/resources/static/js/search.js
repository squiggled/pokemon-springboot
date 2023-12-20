document.addEventListener('DOMContentLoaded', function() {
    var searchInput = document.getElementById('search');
    var searchButton = document.getElementById('searchButton'); 
    var loadMoreButton = document.getElementById('loadMoreButton');
    if (loadMoreButton) {
        loadMoreButton.addEventListener('click', loadMorePokemon);
    }

    function performSearch() {
        var query = searchInput.value; //select the element first, then get value from it

        //perform request to SearchService backend 
        fetch('/search?query=' + encodeURIComponent(query)) //finds a rest controller with the path /search
            .then(response => response.json()) //get response from service
            .then(data => {
                //clear the initial Pokémon list container
                document.getElementById('initialPokemonList').innerHTML = ''; //select the appropriate DOM element
                //update the page with the search results in a diff container
                updateSearchResults(data);
            })
            .catch(error => console.error('Error:', error));
    }
    //add events for 'enter' and clicking search
    if (searchInput) {
        searchInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                performSearch();
            }
        });
    }

    if (searchButton) {
        searchButton.addEventListener('click', performSearch);
    }
});

function updateSearchResults(data) {
    var resultsContainer = document.getElementById('searchResults');
    resultsContainer.innerHTML = '';

    if (data.length === 0) {
        resultsContainer.innerHTML = '<p>No results found.</p>';
        return;
    }

    //iterate thru each json object returned by ajax call. create new html element for each pokeitem
    data.forEach(pokemon => { //each id and details will be mapped w js ${} syntax
        var pokemonElement = createPokemonElement(pokemon);
        resultsContainer.appendChild(pokemonElement);
    });
}

//for loading more
function loadMorePokemon(){
    let offset = document.querySelectorAll('.pokemon-item').length; //count the numebr of elements that have this class applied to it
    fetch(`/load20more?offset=${offset}&limit=20`)
        .then(response => response.json()) //the restcontroller will return 'response
        .then(pokemon => {
            //append new Pokémon to the search results
            pokemon.forEach(pokemon => {
                var pokemonElement = createPokemonElement(pokemon);
                document.getElementById('searchResults').appendChild(pokemonElement);
            });
        })
        .catch(error => console.error('Error:', error));
}
function createPokemonElement(pokemon) {
    var pokemonElement = document.createElement('div');
    pokemonElement.className = 'pokemon-item max-w-sm rounded overflow-hidden shadow-lg bg-white pokemon-item';
    pokemonElement.innerHTML = `
        <a href="/pokemon/${pokemon.id}" class="block">
            <img src="${pokemon.imageUrl}" alt="Pokémon Image" class="w-full">
            <div class="px-6 py-4">
                <div class="font-bold text-xl mb-2">${pokemon.name}</div>
                <div>Type: ${pokemon.type1}</div>
                <div>Type: ${pokemon.type2}</div>
            </div>
        </a>
        <form action="/addToTeam" method="post">
            <input type="hidden" name="pokemonId" value="${pokemon.id}" />
            <button type="submit" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded m-4">
                Add
            </button>
        </form>`;
    return pokemonElement;
}
