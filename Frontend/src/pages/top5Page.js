import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VideoGameClient from "../api/videoGameClient";
import ExampleClient from "../api/exampleClient";

class Top5Page extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['getSuggestion','onFindByName','getTop5', 'renderTop5','renderHighToLow','renderLowToHigh','getHighToLow','getLowToHigh','renderByVideoGameName'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.client = new VideoGameClient();
        //  this.dataStore.addChangeListener(this.renderTop5);
        document.getElementById('searchButton').addEventListener('click', this.onFindByName);
        document.getElementById("H2L").addEventListener('click',this.getHighToLow);
        document.getElementById("L2H").addEventListener('click',this.getLowToHigh);
        document.getElementById("top5").addEventListener('click',this.getTop5);
        document.getElementById("suggest").addEventListener('click',this.getSuggestion);

        this.getTop5();
    }
    async onFindByName(event){
        event.preventDefault();
        event.stopImmediatePropagation();
        let gameName = document.getElementById("searchBarId").value;
        const foundGame = await this.client.getVideoGame(gameName,this.errorHandler);
        this.dataStore.set("VideoGame",foundGame);
        console.log(foundGame);
        if(foundGame){
            this.showMessage("Found Game!")
            localStorage.setItem('foundGame', JSON.stringify(foundGame));
            const game = JSON.parse(localStorage.getItem('foundGame'));
            window.location.href = "searchPage.html";
        } else{
            this.errorHandler("Error creating! Try again... ");
        }
    }
    async renderByVideoGameName(){
        const videoGameResultArea = document.getElementById("searchResult");
        const game = this.dataStore.get("VideoGame");
        console.log(game);
        let upvote = game.UpwardVote;
        let downvote = game.DownwardVote;
        let totalVote = game.TotalVote;
        let votingPercentage = upvote/totalVote * 100;


        videoGameResultArea.innerHTML = `    <div class="centerResults2"><h3>Search Engine Results:</h3></div>
                                                        <div class="centerResults"><img class="rounded" src=${game.image} height="400" width="400"></div>                                                      
                                                          <div><h3>Rating: ${votingPercentage.toFixed(2)}% </h3></div>
                                                          <div><h3> Description: </h3></div>
                                                          <p>${game.Description}</p>
                                                          <div class="centerResults"><h5>Consoles: ${game.Consoles}</h5></div> 
                                                          <div class="game"></div>                                                                                                  
                                                              `
    }

    async renderSuggestion(){
        const leaderboardTable = document.getElementById("leaderboardTable");
        const suggestion = this.dataStore.get("suggestion");
        leaderboardTable.innerHTML = "";
        leaderboardTable.innerHTML = `  <thead>
        <tr>
           <th class="rank">#</th>
            <th class="leaderboardName">Name</th>
            <th class="leaderboardUpvote">Up</th>
            <th class="leaderboardDownvote">Down</th>
            <th class="leaderboardPercentage">%</th>
        </tr>
        </thead>`
        if (suggestion) {
            let upVote = suggestion.UpwardVote;
            let totalVote = suggestion.TotalVote;
            let downVote = suggestion.DownwardVote;
            let votingPercentage = upVote / totalVote * 100;
            votingPercentage = votingPercentage.toFixed(2);
            // slides[i].innerHTML = `<img src=${game.image}>`
            //i++;
            leaderboardTable.innerHTML += `<td>1</td>
                                               <td>${suggestion.name}</td>
                                                <td>${upVote}</td>
                                                <td>${downVote}</td>
                                                 <td>${votingPercentage}%</td>`

        }

    }

    async renderTop5() {
        // let firstSlide = document.getElementById("firstSlide");
        // let secondSlide = document.getElementById("secondSlide");
        // let thirdSlide = document.getElementById("thirdSlide");
        // let fourthSlide = document.getElementById("fourthSlide");
        // let fifthSlide = document.getElementById("fifthSlide");
        //
        //top5results.innerHTML = "";
        const leaderboardTable = document.getElementById("leaderboardTable");
        //const slides = [firstSlide, secondSlide, thirdSlide, fourthSlide, fifthSlide];
        const top5 = this.dataStore.get("top5");
        // let i = 0;
        let x = 1;
        leaderboardTable.innerHTML = "";
        leaderboardTable.innerHTML = `  <thead>
        <tr>
            <th class="rank">#</th>
            <th class="leaderboardName">Name</th>
            <th class="leaderboardUpvote">Up</th>
            <th class="leaderboardDownvote">Down</th>
            <th class="leaderboardPercentage">%</th>
        </tr>
        </thead>`
        if (top5) {
            for (const game of top5) {
                let upVote = game.UpwardVote;
                let totalVote = game.TotalVote;
                let downVote = game.DownwardVote;
                let votingPercentage = upVote / totalVote * 100;
                votingPercentage = votingPercentage.toFixed(2);
                // slides[i].innerHTML = `<img src=${game.image}>`
                //i++;
                leaderboardTable.innerHTML += `<td>${x}</td>
                                               <td>${game.name}</td>
                                                <td>${upVote}</td>
                                                <td>${downVote}</td>
                                                 <td>${votingPercentage}%</td>`
                x++;
            }
        }
    }
    async renderHighToLow() {
        // let top5 = await this.client.getTop5(this.errorHandler);
        //  let firstSlide = document.getElementById("firstSlide");
        //  let secondSlide = document.getElementById("secondSlide");
        //  let thirdSlide = document.getElementById("thirdSlide");
        //  let fourthSlide = document.getElementById("fourthSlide");
        //  let fifthSlide = document.getElementById("fifthSlide");
        //  const slides = [firstSlide, secondSlide, thirdSlide, fourthSlide, fifthSlide];
        //  let i = 0;
        let x = 1;
        // if (top5) {
        //     for (const game of top5) {
        //         let upVote = game.UpwardVote;
        //         let totalVote = game.TotalVote;
        //         let downVote = game.DownwardVote;
        //         let votingPercentage = upVote / totalVote * 100;
        //         votingPercentage = votingPercentage.toFixed(2);
        //         slides[i].innerHTML = `<img src=${game.image}>`
        //         i++;
        //     }
        // }

        const leaderboardTable = document.getElementById("leaderboardTable");
        const highToLow = this.dataStore.get("H2L");
        leaderboardTable.innerHTML = "";
        leaderboardTable.innerHTML = `  <thead>
        <tr>
            <th class="rank">#</th>
            <th class="leaderboardName">Name</th>
            <th class="leaderboardUpvote">Up</th>
            <th class="leaderboardDownvote">Down</th>
            <th class="leaderboardPercentage">%</th>
        </tr>
        </thead>`
        if (highToLow) {
            for (const game of highToLow) {
                let upVote = game.UpwardVote;
                let totalVote = game.TotalVote;
                let downVote = game.DownwardVote;
                let votingPercentage = upVote / totalVote * 100;
                votingPercentage = votingPercentage.toFixed(2);
                leaderboardTable.innerHTML += `<td>${x}</td>
                                               <td>${game.name}</td>
                                                <td>${upVote}</td>
                                                <td>${downVote}</td>
                                                 <td>${votingPercentage}%</td>`
                x++;
            }
        }
    }
    async renderLowToHigh() {
        // let top5 = await this.client.getTop5(this.errorHandler);
        // let firstSlide = document.getElementById("firstSlide");
        // let secondSlide = document.getElementById("secondSlide");
        // let thirdSlide = document.getElementById("thirdSlide");
        // let fourthSlide = document.getElementById("fourthSlide");
        // let fifthSlide = document.getElementById("fifthSlide");
        // const slides = [firstSlide, secondSlide, thirdSlide, fourthSlide, fifthSlide];
        // let i = 0;
        let x = 1;
        // if (top5) {
        //     for (const game of top5) {
        //         let upVote = game.UpwardVote;
        //         let totalVote = game.TotalVote;
        //         let downVote = game.DownwardVote;
        //         let votingPercentage = upVote / totalVote * 100;
        //         votingPercentage = votingPercentage.toFixed(2);
        //         slides[i].innerHTML = `<img src=${game.image}>`
        //         i++;
        //     }
        // }

        const leaderboardTable = document.getElementById("leaderboardTable");
        const lowToHigh = this.dataStore.get("L2H");
        leaderboardTable.innerHTML = "";
        leaderboardTable.innerHTML = `  <thead>
        <tr>
            <th class="rank">#</th>
            <th class="leaderboardName">Name</th>
            <th class="leaderboardUpvote">Up</th>
            <th class="leaderboardDownvote">Down</th>
            <th class="leaderboardPercentage">%</th>
        </tr>
        </thead>`
        if (lowToHigh) {
            for (const game of lowToHigh) {
                let upVote = game.UpwardVote;
                let totalVote = game.TotalVote;
                let downVote = game.DownwardVote;
                let votingPercentage = upVote / totalVote * 100;
                votingPercentage = votingPercentage.toFixed(2);
                leaderboardTable.innerHTML += `<td>${x}</td>
                                               <td>${game.name}</td>
                                                <td>${upVote}</td>
                                                <td>${downVote}</td>
                                                 <td>${votingPercentage}%</td>`
                x++;
            }
        }
    }
    async getHighToLow(event){
        this.dataStore.set("H2L",null);
        let result = await this.client.getHighToLow(this.errorHandler);
        this.dataStore.set("H2L",result);
        console.log(result);
        this.renderHighToLow();
    }
    async getLowToHigh(event){
        this.dataStore.set("L2H",null);
        let result = await this.client.getLowToHigh(this.errorHandler);
        this.dataStore.set("L2H",result);
        console.log(result);
        this.renderLowToHigh();
    }


    async getTop5(event) {
        // Prevent the page from refreshing on form submit
        //event.preventDefault();
        this.dataStore.set("top5",null);
        let result = await this.client.getTop5(this.errorHandler);
        this.dataStore.set("top5",result);
        console.log(result);
        this.renderTop5();

    }

    async getSuggestion(event) {
        // Prevent the page from refreshing on form submit
        //event.preventDefault();
        this.dataStore.set("suggestion",null);
        let result = await this.client.getSuggestion(this.errorHandler);
        this.dataStore.set("suggestion",result);
        console.log(result);
        this.renderSuggestion();

    }

}
const main = async () => {
    const top5Page = new Top5Page();
    await top5Page.mount();
};

window.addEventListener('DOMContentLoaded', main);