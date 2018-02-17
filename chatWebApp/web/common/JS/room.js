var refreshRate = 1000; //mili seconds


function LeaveRoom()
{
    $.ajax({
        url: '/LeaveRoom',
        timeout: 7000,
        error: function(){
            console.log("Failed to send ajax");
        },
        success: function(response) {
            console.info(response);
            window.location.href = '/pages/PokerLobby/lobby.html'
        }
    });
}

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

function ajaxGameManagerSettings() {
    $.ajax({
        url: '/gameManagerSettings',
        success: function(games) {
            refreshGameManagerSettings(games);
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
    document.getElementById("handNumber").innerText=pokerHand.handNumber;


}

function refreshGameManagerSettings(games)
{
    document.getElementById("maxPot").innerText=games.maxPot;
}

function refreshGameSettings(games) {
    document.getElementById("gameTitle").innerText= games.gameTitle;
    document.getElementById("bigValue").innerText= games.structure.blindes.big;
    document.getElementById("smallValue").innerText= games.structure.blindes.small;
    document.getElementById("statusValue").innerText= games.status;
    document.getElementById("handsCount").innerText=games.structure.handsCount;

    if (games.status.toLowerCase()=='waiting')
    {
        document.getElementById("callButton").disabled=true;
        document.getElementById("checkButton").disabled=true;
        document.getElementById("foldButton").disabled=true;
        document.getElementById("raiseButton").disabled=true;
        document.getElementById("raiseInput").disabled=true;

        document.getElementById("betButton").disabled=true;
        document.getElementById("betInput").disabled=true;

    }

    else
    {
        document.getElementById("callButton").disabled=false;
        document.getElementById("checkButton").disabled=false;
        document.getElementById("foldButton").disabled=false;

        document.getElementById("raiseButton").disabled=false;
        document.getElementById("raiseInput").disabled=false;

        document.getElementById("betButton").disabled=false;
        document.getElementById("betInput").disabled=false;
    }
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
    setInterval(ajaxGameManagerSettings, refreshRate);

    //The users list is refreshed automatically every second
    setInterval(ajaxPokerHand, refreshRate);

});