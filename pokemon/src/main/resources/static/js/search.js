document.addEventListener('DOMContentLoaded', function() {
    var searchInput = document.getElementById('search');
    var searchButton = document.getElementById('searchButton'); 

    function performSearch() {
        var query = searchInput.value;

        //perform request to SearchService backend 
        fetch('/search?query=' + encodeURIComponent(query)) //find a rest controller with the path /search
            .then(response => response.json()) //get response from service
            .then(data => {
                //clear the initial Pokémon list container
                document.getElementById('initialPokemonList').innerHTML = '';
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

    data.forEach(function(pokemon) {
        var pokemonElement = document.createElement('div');
        pokemonElement.className = 'max-w-sm rounded overflow-hidden shadow-lg bg-white';
        //data here CANNOT contain thymeleaf as th is only processed on server side, 
        //th renders on server side, then it leaves n ghosts u forever :(
        pokemonElement.innerHTML = `
            <a href="/pokemon/${pokemon.id}" class="block"> <!-- Link to Pokémon detail page -->
                <img src="${pokemon.imageUrl}" alt="Pokémon Image" class="w-full">
                <div class="px-6 py-4">
                    <div class="font-bold text-xl mb-2">${pokemon.name}</div>
                    <div>Type: ${pokemon.type1}</div>
                    <div>Type: ${pokemon.type2}</div>
                </div>
            </a>`;
        resultsContainer.appendChild(pokemonElement);
    });
}