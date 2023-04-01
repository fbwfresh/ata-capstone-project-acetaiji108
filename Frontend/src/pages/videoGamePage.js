import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VideoGameClient from "../api/videoGameClient";

class VideoGamePage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onFindByName', 'renderVideoGames', 'renderByVideoGameName','onUpvote','onDownvote','getAllGames'], this);
        this.dataStore = new DataStore();
    }

    mount() {
        console.log("start of mount");
        this.client = new VideoGameClient();
        this.dataStore.addChangeListener(this.renderVideoGames);
        // this.renderVideoGames();
        this.getAllGames();
        document.getElementById('searchButton').addEventListener('click', this.onFindByName);

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
    async renderVideoGames(){
        console.log("before datastore");
        const allGames = this.dataStore.get("allVideoGames");
        console.log(allGames);
       let GamesHtml =  ""
        if(allGames){
            for (const game of allGames){
                let upvoteButtonId = await  this.replaceSpace(game.name+"upvote");
                let downvoteButtonId = await  this.replaceSpace(game.name+"downvote");

                GamesHtml += `<div><img class= "rounded" src=${game.image} width="150" height="150"></div>
                              <div class="border"><button id= ${upvoteButtonId}>upvote</button>
                               <button id= "${downvoteButtonId}">downvote</button></div>
                              
                                <div class="game"></div>`

    }
    }else{
            GamesHtml =`Loading Games...`;
        }
        document.getElementById("allGames").innerHTML  = GamesHtml;
        if(allGames) {
            for (const game of allGames) {
                let buttonUpId = await  this.replaceSpace(game.name+"upvote");
                let buttonDownId = await  this.replaceSpace(game.name+"downvote");
                const buttonUp = document.getElementById(buttonUpId);
                const buttonDown = document.getElementById(buttonDownId);

                buttonUp.myName = game.name;
                buttonUp.myUpvotes = game.UpwardVote;
                buttonUp.myTotalVotes = game.TotalVote;
                buttonUp.myImage = game.image;
                buttonUp.myDescription =game.Description;
                buttonUp.myConsoles = game.Consoles;
                buttonUp.myDownVotes = game.DownwardVote;

                buttonDown.myName = game.name;
                buttonDown.myDownVotes = game.DownwardVote;
                buttonDown.myTotalVotes = game.TotalVote;
                buttonDown.myDescription = game.Description;
                buttonDown.myImage = game.image;
                buttonDown.myConsoles = game.Consoles;
                buttonDown.myUpvotes = game.UpwardVote;

                buttonUp.addEventListener('click',this.onUpvote);
                buttonDown.addEventListener('click',this.onDownvote);
            }
        }
}

    async replaceSpace(name){
       return name.replace(/ /g,"-");
    }

      async getAllGames(event){
          this.dataStore.set("allVideoGames",null);
          let result = await this.client.getAllVideoGames(this.errorHandler);
          this.dataStore.set("allVideoGames",result);
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
            this.renderByVideoGameName();
        } else{
            this.errorHandler("Error creating! Try again... ");
        }
    }


    async onUpvote(event){
        event.preventDefault();
        console.log("upvote in creation");
        console.log(event.currentTarget.myName);
        console.log(event.currentTarget.myUpvotes);
        console.log(event.currentTarget.myDownVotes);
        console.log(event.currentTarget.myDescription);
        console.log(event.currentTarget.myTotalVotes);
        console.log(event.currentTarget.myImage);
        console.log(event.currentTarget.myConsoles);
        //not executing this method .updateVideoGameUpvote
        //this.dataStore.set(event.currentTarget.myName,event.currentTarget);
       event.currentTarget.myUpvotes = event.currentTarget.myUpvotes + 1;
       event.currentTarget.myTotalVotes = event.currentTarget.myTotalVotes +1;
       let name = event.currentTarget.myName;
       let description = event.currentTarget.myDescription;
       let consoles = event.currentTarget.myConsoles;
       let downvote = event.currentTarget.myDownVotes;
       let image = event.currentTarget.myImage;
       let upvote = event.currentTarget.myUpvotes;
       let totalvote = event.currentTarget.myTotalVotes;

       const updatedGame = await this.client.updateVideoGame(name,description,image,consoles,upvote,downvote,totalvote,this.errorHandler)
       // console.log(this.errorHandler);
        this.dataStore.set(name,updatedGame);
        console.log(updatedGame);
        console.log(upvote);
        console.log(totalvote);
        // event.stopImmediatePropagation();
    }
    async onDownvote(event) {
        //console.log(event);
        event.preventDefault();
        console.log("downvote in creation");
        console.log(event.currentTarget.myName);
        console.log(event.currentTarget.myDownVotes);
        let downvote = event.currentTarget.myDownVotes + 1;
        let totalVote = event.currentTarget.myTotalVotes +1;
        let name = event.currentTarget.myName;
        let description = event.currentTarget.myDescription;
        let consoles = event.currentTarget.myConsoles;
        let image = event.currentTarget.myImage;
        let upvote = event.currentTarget.myUpvotes;
       const updatedGame = await this.client.updateVideoGame(name,description,image,consoles,upvote,downvote,totalVote,this.errorHandler);
        console.log(updatedGame);
        console.log("total vote " + totalVote);
        console.log("downVote " + downvote);
        this.dataStore.set(name,updatedGame)
    }
}
const main = async () => {
    const videoGamePage = new VideoGamePage();
    videoGamePage.mount();
};
window.addEventListener('DOMContentLoaded', main);

