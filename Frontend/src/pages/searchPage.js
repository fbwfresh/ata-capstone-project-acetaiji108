import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VideoGameClient from "../api/videoGameClient";

class SearchPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onFindByName', 'renderByVideoGameName'], this);
        this.dataStore = new DataStore();
    }

    mount() {
        console.log("start of mount");
        this.client = new VideoGameClient();
//        this.dataStore.addChangeListener(this.renderVideoGames);
         this.renderByVideoGameName();
//        this.getAllGames();
//        document.getElementById('recommendButton').addEventListener('click', this.recommendGame);
//        document.getElementById('recommendButtonName').addEventListener('click', this.recommendGameName);
        document.getElementById('searchButton').addEventListener('click', this.onFindByName);
//        this.getTop5();
    }
    async renderByVideoGameName(){
        const game = JSON.parse(localStorage.getItem('foundGame'));
        console.log(game);
        const videoGameResultArea = document.getElementById("searchResult");
//        const game = this.dataStore.get("VideoGame");
        let upvote = game.UpwardVote;
        let downvote = game.DownwardVote;
        let totalVote = game.TotalVote;
        let votingPercentage = upvote/totalVote * 100;


        videoGameResultArea.innerHTML = `
                                                        <div class="centerResults2"><h3>Search Engine Results:</h3></div>
                                                        <div class="centerResults"><img class="rounded" src=${game.image} height="400" width="400"></div>
                                                          <div><h3>Rating: ${votingPercentage.toFixed(2)}% </h3></div>
                                                          <div><h3> Description: </h3></div>
                                                          <p>${game.Description}</p>
                                                          <div class="centerResults"><h5>Consoles: ${game.Consoles}</h5></div>
                                                          <div class="game"></div>
                                                              `
    }

    async replaceSpace(name){
       return name.replace(/ /g,"-");
    }

    async onFindByName(event){
        event.preventDefault();
        event.stopImmediatePropagation();
        let gameName = document.getElementById("searchBarId").value;
        const loadingElement = document.getElementById("loading");
        loadingElement.style.display = "block";
        const foundGame = await this.client.getVideoGame(gameName,this.errorHandler);
        this.dataStore.set("VideoGame",foundGame);
        console.log(foundGame);
        if(foundGame){
            this.showMessage("Found Game!")
//            window.location.href = "searchPage.html";
            this.renderByVideoGameName();
        } else{
            this.errorHandler("Error creating! Try again... ");
        }
        loadingElement.style.display = "none";
    }

}
const main = async () => {
    const searchPage = new SearchPage();
    searchPage.mount();
};
window.addEventListener('DOMContentLoaded', main);