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
        //this.dataStore.addChangeListener(this.renderVideoGames);
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
        const gamesContainer = document.createElement('div');
        gamesContainer.classList.add('games-container');

        for (const game of allGames) {

            const gameContainer = document.createElement('div');
            gameContainer.classList.add('game-container');

            const gameContainerUpvote = document.createElement('div');
            gameContainer.classList.add('game-container-upvote');

            const gameImage = document.createElement('img');
            gameImage.classList.add('game-image');
            gameImage.classList.add('roundedCenter');
            gameImage.src = game.image;

            const gameInfoContainer = document.createElement('div');
            gameInfoContainer.classList.add('game-info-container');

            const gameHeaderContainer = document.createElement('div');
            gameHeaderContainer.classList.add('game-header-container');

            const buttonContainer = document.createElement('div');
            buttonContainer.classList.add('border');

            const upvoteButton = document.createElement('button');

            upvoteButton.textContent = "upvote";
//            upvoteButton.background-color = #4CAF50;
//            upvoteButton.id = await this.replaceSpace(game.name + 'upvote');
//            upvoteButton.innerHTML = "&#8593;";
            upvoteButton.width = 50;
            upvoteButton.height = 50;

            const downvoteButton = document.createElement('button');
            downvoteButton.classList.add('button2');
            downvoteButton.textContent = "downvote";
            downvoteButton.color = "red";
//            downvoteButton.id = await this.replaceSpace(game.name + 'downvote');
//            downvoteButton.innerHTML = "&#8595;";
            downvoteButton.width = 50;
            downvoteButton.height = 50;


            const gameName = document.createElement('h3');
            gameName.textContent =  game.name;

            const gameDescription = document.createElement('p');
            gameDescription.textContent =  game.Description;

            const gameConsoles = document.createElement('h5');
            gameConsoles.textContent = 'Consoles: ' + game.Consoles;


            buttonContainer.appendChild(upvoteButton);
            buttonContainer.appendChild(downvoteButton);


//            gameContainer.appendChild(gameImage);
            gameContainer.appendChild(gameContainerUpvote);
            gameContainerUpvote.appendChild(gameImage);
//            gameContainerUpvote.appendChild(buttonContainer);
            gameContainer.appendChild(gameInfoContainer);
            gameInfoContainer.appendChild(buttonContainer)
            gameInfoContainer.appendChild(gameName)
//            gameInfoContainer.appendChild(gameHeaderContainer);
//            gameHeaderContainer.appendChild(buttonContainer);
//            gameHeaderContainer.appendChild(gameName);
            gameInfoContainer.appendChild(gameDescription);
            gameInfoContainer.appendChild(gameConsoles);

            gamesContainer.appendChild(gameContainer);

            document.body.appendChild(gamesContainer);

            const style = document.createElement('style');
            style.textContent = `
                .games-container {
                  display: flex;
                  flex-wrap: wrap;
                }
                .game-container-upvote {
                  display: flex;
                  flex-direction: row;
                  align-items: center;
                  margin: 10px;
                }
                .game-container {
//                  display: flex;
//                  flex-direction: row;
                  align-items: center;
                  margin: 10px;
                }


                button {
                  background-color: #4CAF50;
                  margin: .3374px;
                  width: 65px;
                }

                .button2 {
                  background-color: #f44336;
                  width: 65px;
                }


                .game-header-container {
                    display: flex;
                  flex-direction: row;
                }
                .game-info-container {
                  margin: 10px;
                }

                .game-image {
                  width: 200px;
                  height: 200px;
                  object-fit: cover;
                }

               .rounded {
                   border-radius: 20px;
                   /*border: 2px solid black;*/
                   margin-bottom: 20px;
               }

                .roundedCenter {
                    border-radius: 20px;
                    margin-bottom: 20px;
                    position: relative;
                }

              `;

            document.head.appendChild(style);
        }

}
    else{

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
          const loadingElement = document.getElementById("loading");
          loadingElement.style.display = "block";
          let result = await this.client.getAllVideoGames(this.errorHandler);
          this.dataStore.set("allVideoGames",result);
          this.renderVideoGames();
          loadingElement.style.display = "none";
      }
 
    async onFindByName(event){
        event.preventDefault();
        event.stopImmediatePropagation();
        let gameName = document.getElementById("searchBarId").value;
        const loadingElement = document.getElementById("loading2");
        loadingElement.style.display = "block";
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
        loadingElement.style.display = "none";
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

