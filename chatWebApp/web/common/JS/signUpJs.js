$(function() {
    $("#addUser").submit(function() {

        var parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: buildUrlWithContextPath("login"),
            //url: this.action,

            error: function(jqXHR) {
                console.error("Failed to submit");
                    $("#result").text(jqXHR.responseText);
            },

            success: function(r) {
                var s= buildUrlWithContextPath("lobby");
                console.info(s);

                window.location.href=s+".html";

            }
        });

        return false;

    })
});


function go()
{

    if (document.getElementsByName("userName").files.length == 0)
    {
        alert ("You must enter a player name. Please try again.");
        document.form1.userName.focus();
        return;
    }

    if (document.getElementsByName("playerType").files.length == 0)
    {
        alert ("You must choose a player type. Please try again");
        document.form1.typePlayer.focus();
        return;
    }


    document.form1.submit();
}

