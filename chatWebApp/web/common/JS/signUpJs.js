
function go()
{
    if (document.form1.userName.value =="")
    {
        alert ("You must enter a player name. Please try again");
        document.form1.userName.focus();
        return;
    }

    if (document.form1.typePlayer.value =="")
    {
        alert ("You must choose a player type. Please try again");
        document.form1.typePlayer.focus();
        return;
    }


    document.form1.submit();
}


