var refreshRate = 1000; //mili seconds
var refreshRateForPokerHand = 3000; //mili seconds

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

function ajaxPokerHand() {
    $.ajax({
        url: '/getPokerHand',
        success: function(pokerHand) {
            refreshPokerHandSettings(pokerHand);
        }
    });
}

function refreshPokerHandSettings(pokerHand) {
    console.info(pokerHand);
    var cards=pokerHand.stringTableCards;
    $.each(cards || [], function(index,value) {
        if (value!='??')
        {
            var loc="../../common/images/cards/"+value+".png";
            document.getElementById("crad"+(index+1)).src=loc;
        }
        console.info(value);
    });

    var players=pokerHand.players;
    $.each(players||[], function (index,value){
        var seatID="seat"+(index+1);
        document.getElementById(seatID).children[0].textContent=value.name;
        document.getElementById(seatID).children[1].textContent=value.type;
        document.getElementById(seatID).children[2].textContent=value.state;
        document.getElementById(seatID).children[3].textContent=value.chips;
        document.getElementById(seatID).children[4].textContent=value.numbersOfBuy;
        document.getElementById(seatID).children[5].textContent=value.handWon;

    });

}

function refreshGameSettings(games) {
    document.getElementById("gameTitle").innerText= games.gameTitle;
    document.getElementById("bigValue").innerText= games.structure.blindes.big;
    document.getElementById("smallValue").innerText= games.structure.blindes.small;
    document.getElementById("statusValue").innerText= games.status;
}

function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(username, playerType) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + username+ ' ('+playerType + ')'+'</li>').appendTo($("#userslist"));
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

    //The users list is refreshed automatically every second
    setInterval(ajaxPokerHand, refreshRateForPokerHand);

});