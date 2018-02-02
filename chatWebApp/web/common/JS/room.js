var refreshRate = 1000; //mili seconds

function ajaxUsersList() {
    $.ajax({
        url: '/gameuserlist',
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function ajaxGameSettings() {
    $.ajax({
        url: '/gameSettings',
        success: function(games) {
            refreshGameSettings(games);
        }
    });
}

function refreshGameSettings(games) {

    $.each(games || [], function(index, games) {

        $('<tr>').html(
            "<td align=center>" + games.gameTitle + "</td>" +
            "<td align=center>" + games.uploadedBy + "</td>" +
            "<td align=center>" + games.structure.handsCount + "</td>" +
            "<td align=center>" + games.numberOfPlayers + "</td>"+
            "<td align=center>" + games.registeredPlayers + "</td>" +
            "<td align=center>" + games.structure.blindes.big + "</td>" +
            "<td align=center>" + games.structure.blindes.small + "</td>" +
            "<td align=center>" + games.structure.buy + "</td>"+
            "<td align=center>" + games.structure.blindes.fixed +"</td>"+
            "<td align=center><button class='joinGame' onclick='joinGame(this.value)' value='"+games.gameTitle+"'>Join Game</button></td>")
            .appendTo('#games_table');
    });
}

function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(username, playerType) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + username+ ' ('+playerType + '</li>').appendTo($("#userslist"));
    });
}


//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

    //The users list is refreshed automatically every second
    setInterval(ajaxGameSettings, refreshRate);

});