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
    document.getElementById("gameTitle").innerText= games.gameTitle;
    document.getElementById("bigValue").innerText= games.structure.blindes.big;
    document.getElementById("smallValue").innerText= games.structure.blindes.small;


    /* games.uploadedBy
    games.structure.handsCount
    games.numberOfPlayers
    games.registeredPlayers
    games.structure.buy
    */
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