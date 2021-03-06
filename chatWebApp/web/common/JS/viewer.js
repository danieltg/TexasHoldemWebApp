var refreshRate = 1000; //mili seconds

var flag = true;
var showCards=true;
var showLeaveAndBuy=true;
var chatVersion = 0;
var showButtons=true;

function LeaveRoom()
{
    $.ajax({
        url: buildUrlWithContextPath("LeaveRoom"),
        timeout: 7000,
        error: function(){
            console.log("Failed to send ajax");
        },
        success: function(response) {
            var s= buildUrlWithContextPath("lobby");
            console.info(s);
            window.location.href=s+".html";
        }
    });
}

function ajaxUsersList() {
    $.ajax({
        url: buildUrlWithContextPath("gameuserlist"),
        success: function(info) {
            refreshUsersList(info);
        }
    });
}

function ajaxViewerList() {
    $.ajax({
        url: buildUrlWithContextPath("gameViewers"),
        success: function(info) {
            refreshViewerList(info);
        }
    });
}

function ajaxGameSettings() {
    $.ajax({
        url: buildUrlWithContextPath("gameSettings"),
        success: function(games) {
            refreshGameSettings(games);
        }
    });
}

function ajaxGameManagerSettings() {
    $.ajax({
        url: buildUrlWithContextPath("gameManagerSettings"),
        success: function(games) {
            refreshGameManagerSettings(games);
        }
    });
}

function ajaxPokerHand() {
    $.ajax({
        url: buildUrlWithContextPath("getPokerHand"),
        success: function(pokerHand) {
            refreshPokerHandSettings(pokerHand);
        }
    });
}

function ajaxPlayerInfo() {
    $.ajax({
        url: buildUrlWithContextPath("getPlayerInfo"),
        success: function(player) {
            if (player!=null)
            {
                refreshPlayerInfo(player);
            }
        }
    });
}

function refreshPlayerInfo(player)
{
    var myTurn=player.isMyTurn;

    if (player.type.toLowerCase()=="computer")
    {
        showLeaveAndBuy=false;
    }
    var cards=player.stringholeCards;
    $.each(cards || [], function(index,value) {
        if (value!='??' && showCards)
        {
            //var loc="../../common/images/cards/"+value+".png";
            var loc= buildUrlWithContextPath("common/images/cards/");
            loc= loc +value+".png";
            document.getElementById("player_crad"+(index+1)).src=loc;
        }
    });


    if (myTurn)
    {
        if (showButtons)
        {
            disableAllButtons();

            var options=player.options;
            $.each(options||[], function (index,value){
                if(value=='R')
                {
                    document.getElementById("raiseInput").value="1";
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
                    document.getElementById("betInput").value="1";
                    document.getElementById("betButton").disabled=false;
                    document.getElementById("betInput").disabled=false;
                    document.getElementById("betInput").max=player.maxBet;
                    document.getElementById("maxValueLabel").innerText="The max bet is: "+player.maxBet;
                }
            });

            showButtons=false;
        }
    }
    else //it's not my turn- I have to disable all buttons
    {
        disableAllButtons();
    }
}

function refreshPokerHandSettings(pokerHand) {
    var cards=pokerHand.stringTableCards;
    if (pokerHand.state.toLowerCase()!="end" && pokerHand.state.toLowerCase()!="gameover" )
    {
        showCards=true;

        $.each(cards || [], function(index,value) {
            if (value!='??')
            {
                var loc=buildUrlWithContextPath("common/images/cards/")+value+".png";
                document.getElementById("crad"+(index+1)).src=loc;
            }
        });
    }

    var players=pokerHand.players;

    $("#userslist").empty();
    $.each(players||[], function (index,value){

        if (value.isMyTurn)
        {
            $('<li><b>' + value.name+ ' ('+value.type + ')'+'</b></li>').appendTo($("#userslist"));
        }
        else if (value.leave==false)
        {
            $('<li>' + value.name+ ' ('+value.type + ')'+'</li>').appendTo($("#userslist"));
        }

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

        var cards=value.stringholeCards;
        $.each(cards || [], function(index,value) {
            if (value!='??' && showCards)
            {
                //var loc="../../common/images/cards/"+value+".png";
                var loc=buildUrlWithContextPath("common/images/cards/")+value+".png";
                document.getElementById(seatID+"_crad"+(index+1)).src=loc;
                document.getElementById(seatID+"_crad"+(index+1)).style.visibility = "visible";
            }
        });

    });
    document.getElementById("handNumber").innerText=pokerHand.handNumber;
    document.getElementById("Pot").innerText=pokerHand.pot;

    if (pokerHand.lastAction=='R' || pokerHand.lastAction=='B')
    {
        var num= pokerHand.lastActionInfo;
        var toStr=pokerHand.lastAction+ " ("+num+")";
        document.getElementById("lastAction").innerText=toStr;
    }
    else
    {
        document.getElementById("lastAction").innerText=pokerHand.lastAction;
    }

    var state=pokerHand.state;

    if (state.toLowerCase()=="end")
    {
        updatePageWithHandEnd();
    }

    else if (state.toLowerCase()=="gameover")
    {
        updatePageWithGameOver();
        var s= buildUrlWithContextPath("roomGameViewer");
        console.info(s);
        window.location.href=s+".html";
    }
}


function updatePageWithGameOver()
{
    $.ajax({
        url: buildUrlWithContextPath("getMessageToDisplay"),
        timeout: 7000,
        error: function(){
            console.log("User already got this message");
        },
        success: function(response) {
            var answer= confirm(response);
            if (answer)
            {
                console.log("User confirmed the answer");
            }
        }
    });

}

function closePopUP()
{
    var popup = document.getElementById("myPopup");
    popup.innerText="";
    popup.classList.toggle("show");

    clearCards();
    clearPlayersCard();
}

function clearPlayersCard()
{

    for (index = 1; index<7; index++)
    {
        var seatID="seat"+(index);
        document.getElementById(seatID).children[0].textContent="";
        document.getElementById(seatID).children[1].textContent="";
        document.getElementById(seatID).children[2].textContent="";
        document.getElementById(seatID).children[3].textContent="";
        document.getElementById(seatID).children[4].textContent="";
        document.getElementById(seatID).children[5].textContent="";
        document.getElementById(seatID).style.backgroundColor = "darkgray";
    }


}

function buy() {
    $.ajax({
        url: buildUrlWithContextPath("buy"),
        timeout: 2000,

        success: function(response) {
            alert("Operation successfully completed!");
            document.getElementById("closePop").focus();
        }
    });
}

function updatePageWithHandEnd()
{
    $.ajax({
        url: buildUrlWithContextPath("getMessageToDisplay"),
        timeout: 7000,
        error: function(){
            console.log("User already got this message");
        },
        success: function(response) {
            var name=(response.split("&&&"))[0];
            var messageTxt=(response.split("&&&"))[1];

            var message="<p><b>"+name+"</b></p>"+
                "<p>"+messageTxt+"</p>";

            var popup = document.getElementById("myPopup");
            popup.innerHTML=message;

            var closePopUp = document.createElement("button");
            closePopUp.id="closePop";
            closePopUp.innerHTML = "Close";
            closePopUp.onclick = closePopUP;

            popup.appendChild(closePopUp);
            popup.classList.toggle("show");

        }
    });

}

function clearCards()
{
    var loc=buildUrlWithContextPath("common/images/back.png");

    //var loc="../../common/images/back.png";
    document.getElementById("seat1_crad1").src=loc;
    document.getElementById("seat1_crad2").src=loc;

    document.getElementById("seat2_crad1").src=loc;
    document.getElementById("seat2_crad2").src=loc;

    document.getElementById("seat3_crad1").src=loc;
    document.getElementById("seat3_crad2").src=loc;

    document.getElementById("seat4_crad1").src=loc;
    document.getElementById("seat4_crad2").src=loc;

    document.getElementById("seat5_crad1").src=loc;
    document.getElementById("seat5_crad2").src=loc;

    document.getElementById("seat6_crad1").src=loc;
    document.getElementById("seat6_crad2").src=loc;

    document.getElementById("crad1").src=loc;
    document.getElementById("crad2").src=loc;
    document.getElementById("crad3").src=loc;
    document.getElementById("crad4").src=loc;
    document.getElementById("crad5").src=loc;
    showCards=false;
}



function refreshGameManagerSettings(games)
{
    document.getElementById("maxPot").innerText=games.maxPot;
}

function updateSelection(action, info)
{
    showButtons=true;

    console.info("Selection is: "+action +" ,info: "+info);

    $.ajax({
        url: buildUrlWithContextPath("updatePlayerSelection"),
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

function removeUserFromRoom() {

}

function closePopUPonGameOver() {

    console.log("Game over");
    LeaveRoom();
}

function refreshGameSettings(games) {
    document.getElementById("gameTitle").innerText= games.gameTitle;
    document.getElementById("bigValue").innerText= games.structure.blindes.big;
    document.getElementById("smallValue").innerText= games.structure.blindes.small;
    document.getElementById("statusValue").innerText= games.status;
    document.getElementById("handsCount").innerText=games.structure.handsCount;

    ajaxViewerList();
    var status=games.status.toLowerCase();

    if (status.toLowerCase()=="waiting")
    {
        clearCards();
        clearPlayersCard();
        ajaxUsersList();
    }
    else if ((status.toLowerCase()=="ended" && flag )|| (status.toLowerCase()=="running" && games.registeredPlayers<2 ))
    {
        flag=false;

        var message="<p><b>Hand ended... See you next time</b></p>";

        var popup = document.getElementById("myPopup");
        popup.innerHTML=message;

        var closePopUp = document.createElement("button");
        closePopUp.id="closeIt"
        closePopUp.innerHTML = "Close";
        closePopUp.onclick = closePopUPonGameOver;

        popup.appendChild(closePopUp);
        popup.classList.toggle("show");
    }
}



function refreshViewerList(users) {

    $("#viewers").empty();

    $.each(users || [], function(username, playerType) {
        $('<li>' + username+ ' ('+playerType + ')'+'</li>').appendTo($("#viewers"));
    });

}


function refreshUsersList(info) {

    var users=info.users;
    var active=info.active;

    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(username, playerType) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        if (active==username && playerType.toLowerCase()=="computer")
        {
            showLeaveAndBuy=false;
            $("#leaveButton").hide();
        }

        $('<li>' + username+ ' ('+playerType + ')'+'</li>').appendTo($("#userslist"));
    });



}

function triggerAjaxChatContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

function ajaxChatContent() {
    $.ajax({
        url: buildUrlWithContextPath("chat"),
        data: "chatversion=" + chatVersion,
        dataType: 'json',
        success: function(data) {

            if (data.version !== chatVersion) {
                chatVersion = data.version;
                appendToChatArea(data.entries);
            }
            triggerAjaxChatContent();
        },
        error: function(error) {
            triggerAjaxChatContent();
        }
    });
}

function createChatEntry (entry){
    entry.chatString = entry.chatString.replace (":)", "<span class='smiley'></span>");
    return $("<span class=\"success\">").append(entry.username + "> " + entry.chatString);
}

function appendChatEntry(index, entry){
    var entryElement = createChatEntry(entry);
    $("#chatarea").append(entryElement).append("<br>");
}

//entries = the added chat strings represented as a single string
function appendToChatArea(entries) {
//    $("#chatarea").children(".success").removeClass("success");

    // add the relevant entries
    $.each(entries || [], appendChatEntry);

    // handle the scroller to auto scroll to the end of the chat area
    var scroller = $("#chatarea");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

//activate the timer calls after the page is loaded
$(function() {

    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: buildUrlWithContextPath("sendChat"),
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
            }
        });

        $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });

    //prevent IE from caching ajax calls
    $.ajaxSetup({cache: false});

    //The users list is refreshed automatically every second
    //setInterval(ajaxUsersList, refreshRate);

    //The users list is refreshed automatically every second
    setInterval(ajaxGameSettings, refreshRate);

    //The users list is refreshed automatically every second
    setInterval(ajaxGameManagerSettings, refreshRate);

    //The users list is refreshed automatically every second
    setInterval(ajaxPokerHand, refreshRate);

    setInterval(ajaxPlayerInfo,refreshRate);

    triggerAjaxChatContent();

});