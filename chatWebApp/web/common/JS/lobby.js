var lobbyVersion = 0;
var refreshRate = 1000; //mili seconds


$(function() {
    $("#uploadForm").submit(function() {

        var file1 = this[0].files[0];

        var formData = new FormData();
        formData.append("fake-key-1", file1);

        $.ajax({
            method:'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function(jqXHR) {
                console.error("Failed to submit");
                if(jqXHR.status && jqXHR.status==400) {
                    $("#result").text("Failed to get result from server " + jqXHR.responseText);
                }
            },
            success: function(r) {
                $("#result").text("Your game has been submitted successfully.");
                console.info(r);
            }
        });

        return false;
    })
});

function ajaxUsersList() {
    $.ajax({
        url: '/userlist',
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function ajaxGamesList() {
    $.ajax({
        url: '/gamesList',
        success: function(games) {
            refreshGamesList(games);
        }
    });
}


//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshGamesList(games) {
    //clear all current users
    $("#games_table").empty();
    $('<tr>').html(
        "<th align=center><b>Game Title</b></th>" +
        "<th align=center><b>Uploaded by</b></th>" +
        "<th align=center><b>Hands Count</b></th>" +
        "<th align=center><b>Total Players</b></th>" +
        "<th align=center><b>Registered Players</b></th>" +
        "<th align=center><b>Big</b></th>" +
        "<th align=center><b>Small</b></th>" +
        "<th align=center><b>Buy</b></th>" +
        "<th align=center><b>Fixed Blindes</b></th>" +
        "<th align=center><b>Join Game</b></th>" +
    "</tr>")
        .appendTo('#games_table');

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(games || [], function(index, games) {
        console.log("Adding games #" + index + ": " + games);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        //$('<li>' + username + '</li>').appendTo($("#userslist"));

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
            "<td align=center><button class='joinGame' data-key="+games.gameTitle+">Join Game</button></td>")
            .appendTo('#games_table');
    });
}



//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>').appendTo($("#userslist"));
    });
}

function triggerAjaxTableContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

//activate the timer calls after the page is loaded
$(function() {

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

    setInterval(ajaxGamesList, refreshRate);

});