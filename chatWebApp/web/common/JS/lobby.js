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
                obj = JSON.parse(r);

                $('<tr>').html(
                    "<td>" + obj.gameTitle + "</td>" +
                    "<td>" + obj.uploadedBy + "</td>" +
                    "<td>" + obj.structure.handsCount + "</td>" +
                    "<td>" + obj.numberOfPlayers + "</td>"+
                    "<td>" + obj.registeredPlayers + "</td>" +
                    "<td>" + obj.structure.blindes.big + "</td>" +
                    "<td>" + obj.structure.blindes.small + "</td>" +
                    "<td>" + obj.structure.buy + "</td>"+
                    "<td>" + obj.structure.blindes.fixed +"</td>" )
                    .appendTo('#games_table');

            }
        });

        return false;
    })
})