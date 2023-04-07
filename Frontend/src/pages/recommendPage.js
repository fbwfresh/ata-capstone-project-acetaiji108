import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VideoGameClient from "../api/videoGameClient";

class RecommendPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['recommendGame', 'recommendGameName', 'getTop5', 'onFindByName', 'renderByVideoGameName', 'renderByVideoGameNameRecommended', 'getAllGames'], this);
        this.dataStore = new DataStore();
    }

    mount() {
        console.log("start of mount");
        this.client = new VideoGameClient();
//        this.dataStore.addChangeListener(this.renderVideoGames);
        // this.renderVideoGames();
        this.getAllGames();
        document.getElementById('recommendButton').addEventListener('click', this.recommendGame);
        document.getElementById('recommendButtonName').addEventListener('click', this.recommendGameName);
        document.getElementById('searchButton').addEventListener('click', this.onFindByName);
        this.getTop5();
    }
    async renderByVideoGameName(){
        const videoGameResultArea = document.getElementById("searchResult");
        const game = this.dataStore.get("VideoGame");
        console.log(game);
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

    async renderByVideoGameNameRecommended(){
            const videoGameResultArea = document.getElementById("searchResult");
            const game = this.dataStore.get("VideoGame");
            console.log(game);
            let upvote = game.UpwardVote;
            let downvote = game.DownwardVote;
            let totalVote = game.TotalVote;
            let votingPercentage = upvote/totalVote * 100;


            videoGameResultArea.innerHTML = `
                                                            <div class="centerResults2"><h3>Recommended Game:</h3></div>
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

    async getAllGames(event){
              this.dataStore.set("allVideoGames",null);
              let result = await this.client.getAllVideoGames(this.errorHandler);
              this.dataStore.set("allVideoGames",result);
//              this.renderVideoGames();
          }

    async getTop5(event) {
        this.dataStore.set("top5",null);
        let result = await this.client.getTop5(this.errorHandler);
        this.dataStore.set("top5",result);
        console.log(result);
    }

    async recommendGame() {
        const top5 = this.dataStore.get("top5");

        const numRand5 = Math.floor(Math.random() * 5);
        let recommendedGame = top5[numRand5]; // initialize recommended game to the first game in the top 5 list
        this.dataStore.set("VideoGame", recommendedGame);
        this.renderByVideoGameNameRecommended();

//        window.alert(recommendedGame.name);
    }

    async recommendGameName() {
        const consoleType = document.getElementById('console').value;
        if (consoleType === "Console" || consoleType === "" || consoleType === null) {
            window.alert('Please select a console type');
            return;
        }
        const loadingElement = document.getElementById("loading");
        loadingElement.style.display = "block";
        const allVideoGames = await this.client.getAllVideoGames(this.errorHandler);
        this.dataStore.set("allVideoGames",allVideoGames);
        let gamesWithConsoleType = allVideoGames.filter(game => Array.from(game.Consoles).map(c => c.includes(consoleType)).includes(true));
        if (gamesWithConsoleType.length === 0) {
            window.alert(`No games found with console type ${consoleType}`);
            return;
        }

        const sortedGames = gamesWithConsoleType.sort((a, b) => b.UpwardVote - a.UpwardVote);
        console.log("sortedGames");
        console.log(sortedGames);

        const letter = document.getElementById('fname').value.charAt(0).toUpperCase();
        let gamesStartingWithLetter = sortedGames.filter(game => game.name.charAt(0) === letter);

        if (gamesStartingWithLetter.length === 0) {
            gamesStartingWithLetter = sortedGames.sort((a,b) => {
                const aDist = Math.abs(a.name.charCodeAt(0) - letter.charCodeAt(0));
                const bDist = Math.abs(b.name.charCodeAt(0) - letter.charCodeAt(0));
                return aDist - bDist;
            });
        }
        console.log('gamesStartingWithLetter');
        console.log(gamesStartingWithLetter);
        const recommendedGame = gamesStartingWithLetter[0];

        this.dataStore.set("VideoGame", recommendedGame);
        this.renderByVideoGameNameRecommended();

        loadingElement.style.display = "none";
//        window.alert(recommendedGame.name);
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
            this.renderByVideoGameName();
        } else{
            this.errorHandler("Error creating! Try again... ");
        }
        loadingElement.style.display = "none";
    }

}
const main = async () => {
    const recommendPage = new RecommendPage();
    recommendPage.mount();
};
window.addEventListener('DOMContentLoaded', main);