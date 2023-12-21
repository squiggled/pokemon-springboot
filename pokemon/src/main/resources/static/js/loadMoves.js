
//load moves and update the table
function loadMoves(pokemonId) {
    fetch('/moves/' + pokemonId)
        .then(response => response.json())
        .then(movesList => {
            var movesTableBody = document.getElementById('movesTableBody');
            movesTableBody.innerHTML = ''; 
            movesList.forEach(moveStr => {
                var moveDetails = moveStr.split(',');
                var moveName = (moveDetails[0]);
                var row = `<tr>
                    <td class="border px-4 py-2">${moveName}</td>
                    <td class="border px-4 py-2">${moveDetails[1]}</td>
                    <td class="border px-4 py-2">${moveDetails[2]}</td>
                    <td class="border px-4 py-2">${moveDetails[3]}</td>
                    <td class="border px-4 py-2">${moveDetails[4]}</td>
                </tr>`;
                movesTableBody.innerHTML += row;
            });
            movesTable.classList.remove('hidden');
        })
        .catch(error => console.error('Error loading moves:', error));
}

//event listener for loadmoves
document.addEventListener('DOMContentLoaded', function() {
    var loadMovesBtn = document.getElementById('loadMovesBtn');
    if (loadMovesBtn) {
        loadMovesBtn.addEventListener('click', function() {
            var pokemonId = this.getAttribute('data-pokemon-id'); //add attribute data-pokemon-id in the thymeleaf html to retrieve it here
            loadMoves(pokemonId);
        });
    }
});
