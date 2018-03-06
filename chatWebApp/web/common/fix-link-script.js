
var GET_DATA_WITH_CONTEXT_PATH_URL = buildUrlWithContextPath("getdata4");

window.onload = function() {
    var linkToFix = document.getElementsByClassName("created-with-js")[0];
    linkToFix.href = GET_DATA_WITH_CONTEXT_PATH_URL;
    linkToFix.innerText = "gets the data using dynamically calculated context path: [" + GET_DATA_WITH_CONTEXT_PATH_URL + "]";
};
