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

function ajaxPlayerInfo() {
    $.ajax({
        url: '/getPlayerInfo',
        success: function(player) {
            refreshPlayerInfo(player);
        }
    });
}

function refreshPlayerInfo(player)
{
    console.info(player);
    var myTurn=player.isMyTurn;

    var cards=player.stringholeCards;
    $.each(cards || [], function(index,value) {
        if (value!='??')
        {
            var loc="../../common/images/cards/"+value+".png";
            document.getElementById("player_crad"+(index+1)).src=loc;
        }
    });


    if (myTurn)
    {
        disableAllButtons();
        var options=player.options;
        $.each(options||[], function (index,value){
            if(value=='R')
            {
                document.getElementById("raiseButton").disabled=false;
                document.getElementById("raiseInput").disabled=false;
                document.getElementById("raiseInput").max=player.maxBet;
                document.getElementById("maxValueLabel").innerText="The max raise is: "+player.maxBet;

            }
            else if (value=='F') {document.getElementById("foldButton").disabled=false;}
            else if (value=='C') {document.getElementById("callButton").disabled=false;}
            else if (value=='K') {document.getElementById("checkButton").disabled=false;}
            else if (value=='B')
            {
                document.getElementById("betButton").disabled=false;
                document.getElementById("betInput").disabled=false;
                document.getElementById("betInput").max=player.maxBet;
                document.getElementById("maxValueLabel").innerText="The max bet is: "+player.maxBet;
            }
        });

    }
    else //it's not my turn- I have to disable all buttons
    {
        disableAllButtons();
    }
}

function refreshPokerHandSettings(pokerHand) {
    console.info(pokerHand);
    var cards=pokerHand.stringTableCards;
    if (pokerHand.state.toLowerCase()!="end" && pokerHand.state.toLowerCase()!="gameover" )
    {
        $.each(cards || [], function(index,value) {
            if (value!='??')
            {
                var loc="../../common/images/cards/"+value+".png";
                document.getElementById("crad"+(index+1)).src=loc;
            }
        });
    }

    var players=pokerHand.players;
    $.each(players||[], function (index,value){
        var seatID="seat"+(index+1);
        document.getElementById(seatID).children[0].textContent=value.name;
        document.getElementById(seatID).children[1].textContent="Type: "+value.type;
        document.getElementById(seatID).children[2].textContent="State: "+value.state;
        document.getElementById(seatID).children[3].textContent="Chips: "+value.chips;
        document.getElementById(seatID).children[4].textContent="Buys: "+value.numbersOfBuy;
        document.getElementById(seatID).children[5].textContent="Hands won: "+value.handsWon;
        document.getElementById(seatID).style.backgroundColor = "beige";
        if (value.folded)
        {
            document.getElementById(seatID).style.backgroundColor = "red";
        }

    });
    document.getElementById("handNumber").innerText=pokerHand.handNumber;
    document.getElementById("Pot").innerText=pokerHand.pot;


    var state=pokerHand.state;

    if (state.toLowerCase()=="end")
    {
        updatePageWithHandEnd();
    }

    else if (state.toLowerCase()=="gameover")
    {
        updatePageWithGameOver();
        window.location.href = '/pages/gameRoom/room.html';
    }
}


function updatePageWithGameOver()
{
    $.ajax({
        url: '/getMessageToDisplay',
        timeout: 7000,
        error: function(){
            console.log("User already got this message");
        },
        success: function(response) {
            console.info(response);
            var answer= confirm(response);
            if (answer)
            {
                console.log("User confirmed the answer");
                startNewHand();
            }
        }
    });

}

function updatePageWithHandEnd()
{
    $.ajax({
        url: '/getMessageToDisplay',
        timeout: 7000,
        error: function(){
            console.log("User already got this message");
        },
        success: function(response) {
            console.info(response);

            var win = window.open("", "", "width=200,height=100");
            win.document.write("<p>"+response+"</p>");
            win.focus();


            var timer = setInterval(function() {
                if (win.closed) {
                    clearInterval(timer);
                    alert("Notification window closed !");
                    console.log("User confirmed the answer");
                    clearCards();
                    var delayInMilliseconds = 6000;
                    setTimeout(function() {
                        startNewHand();
                    }, delayInMilliseconds);



                }
            }, 500);

        }
    });

}

function clearCards()
{
    var loc="../../common/images/back.png";
    document.getElementById("player_crad1").src=loc;
    document.getElementById("player_crad2").src=loc;

    document.getElementById("crad1").src=loc;
    document.getElementById("crad2").src=loc;
    document.getElementById("crad3").src=loc;
    document.getElementById("crad4").src=loc;
    document.getElementById("crad5").src=loc;

}
function startNewHand()
{
    $.ajax({
        url: '/startNewHand',
        timeout: 7000,
        error: function(){
            console.log("Failed to send ajax");
        },
        success: function() {
            console.info("We started a new game");
        }
    });
}

function refreshGameManagerSettings(games)
{
    document.getElementById("maxPot").innerText=games.maxPot;
}

function updateSelection(action, info)
{
    console.info("Selection is: "+action +" ,info: "+info);

    $.ajax({
        url: '/updatePlayerSelection',
        data:
            {
                actionToDo: action,
                num: info
            },

        timeout: 7000,
        error: function(){
            console.log("Failed to send ajax");
        },
        success: function(response) {
            document.getElementById("raiseInput").value="";
            document.getElementById("betInput").value="";
            document.getElementById("maxValueLabel").innerText="";

        }
    });

}
function disableAllButtons()
{
    document.getElementById("callButton").disabled=true;
    document.getElementById("checkButton").disabled=true;
    document.getElementById("foldButton").disabled=true;
    document.getElementById("raiseButton").disabled=true;
    document.getElementById("raiseInput").disabled=true;

    document.getElementById("betButton").disabled=true;
    document.getElementById("betInput").disabled=true;

}

function enableAllButtons()
{
    document.getElementById("callButton").disabled=false;
    document.getElementById("checkButton").disabled=false;
    document.getElementById("foldButton").disabled=false;
    document.getElementById("raiseButton").disabled=false;
    document.getElementById("raiseInput").disabled=false;
    document.getElementById("betButton").disabled=false;
    document.getElementById("betInput").disabled=false;
}
function refreshGameSettings(games) {
    document.getElementById("gameTitle").innerText= games.gameTitle;
    document.getElementById("bigValue").innerText= games.structure.blindes.big;
    document.getElementById("smallValue").innerText= games.structure.blindes.small;
    document.getElementById("statusValue").innerText= games.status;
    document.getElementById("handsCount").innerText=games.structure.handsCount;
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

    setInterval(ajaxPlayerInfo,refreshRate)

});