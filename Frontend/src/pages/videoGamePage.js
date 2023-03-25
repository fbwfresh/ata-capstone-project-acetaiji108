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
        // const MonsterHunterUpvote = document.getElementById("MonsterHunterUpvote");
        // MonsterHunterUpvote.addEventListener('click',this.onCreate,false);
        // MonsterHunterUpvote.myName = "MonsterHunterID";
        // MonsterHunterUpvote.myUpvotes = 0;
         //document.getElementById('MonsterHunterUpvote').addEventListener('click', this.onCreate);
        // document.getElementById('downvote').addEventListener('click', this.onFindById);
        // document.getElementById('upvote').addEventListener('click', this.onCreate);
        // document.getElementById('downvote').addEventListener('click', this.onFindById);
        // document.getElementById('upvote').addEventListener('click', this.onCreate);
        // document.getElementById('downvote').addEventListener('click', this.onFindById);
        // document.getElementById('upvote').addEventListener('click', this.onCreate);
        // document.getElementById('downvote').addEventListener('click', this.onFindById);
        // document.getElementById('upvote').addEventListener('click', this.onCreate);
        // document.getElementById('downvote').addEventListener('click', this.onFindById);
        console.log("start of mount");
           // document.addEventListener("DOMContentLoaded", this.renderVideoGames);

        this.client = new VideoGameClient();
        this.dataStore.addChangeListener(this.renderVideoGames)
        // this.renderVideoGames();
        this.getAllGames();
    }
    async renderByVideoGameName(){
        const doctorFoundTable = document.getElementById("doctorFoundByIdResult");
        const doctor = this.dataStore.get("doctor");

        doctorFoundTable.innerHTML += `
                                                         <div><td>${doctor.doctorId}</td> </div>
                                                          <div><td>${doctor.name}</td></div>
                                                          <div><td>${doctor.dob}</td></div>
                                                          <div><td>${doctor.isActive}</td></div>
                                                              `
    }
    async renderVideoGames(){

        console.log("before datastore");
        const allGames = this.dataStore.get("allVideoGames");
        console.log(allGames);
       let GamesHtml =  ""
        if(allGames){
            for (const game of allGames){
                let upvoteName = await  this.replaceSpace(game.name+"upvote");
                let downvoteName = await  this.replaceSpace(game.name+"downvote");

                GamesHtml += `<div><img class= "rounded" src=${game.image} width="150" height="150"></div>
                              <div class="border"><button id= ${upvoteName}>upvote</button>
                               <button id= "${downvoteName}">downvote</button></div>
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
                buttonDown.myName = game.name;
                buttonDown.myDownVotes = game.DownwardVote;
                buttonDown.myTotalVotes = game.TotalVote;
                buttonUp.addEventListener('click',this.onUpvote);
                buttonDown.addEventListener('click',this.onDownvote);
                //buttonDown.addEventListener('click',this.differentmethodto)
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
        let doctorId = document.getElementById("add-id-field").value;
        const foundDoctor = await this.client.getDoctor(doctorId, this.errorHandler);
        this.dataStore.set("doctor",foundDoctor);
        console.log(foundDoctor);
        if(foundDoctor){
            this.showMessage("Found Doctor!")
        } else{
            this.errorHandler("Error creating! Try again... ");
        }
    }

    async onUpvote(event){
        //console.log(event);
        event.preventDefault();
        console.log("in creation");
        console.log(event.currentTarget.myName);
        console.log(event.currentTarget.myUpvotes);
        //not executing this method .updateVideoGameUpvote
       event.currentTarget.myUpvotes = event.currentTarget.myUpvotes + 1;
       event.currentTarget.myTotalVotes = event.currentTarget.myTotalVotes +1;
       let upvote = event.myUpvotes;
       let totalvote = event.myTotalVotes;
      // const updatedVote = await this.client.updateVideoGameUpvote(event.currentTarget.myName);
       // console.log(updatedVote);
        console.log(upvote);
        console.log(totalvote);


        // event.stopImmediatePropagation();
        //I would use an update method from client within this method after i add one upvote and take
        // whatevers needed to identify the game and whatever is being updated,
        // probably just create a new method using the name and adding plus one.
        //event.currentTarget.myU

//         const videoGameElement = dataStore.get(event.currentTarget.myName);
//         // event.currentTarget.myUpvotes = event.currentTarget.myUpvotes + 1;
//
//         let name = document.getElementById("add-doctor-name-field").value;
//         let dob = document.getElementById("add-doctor-dob-field").value;
// //this is where the doctor gets created on the page by inputting the information we saved into variables
//         const createdDoctor = await this.client.createDoctor(name, dob, this.errorHandler);
//         //Setting updating the value in doctors to be the new created doctor
//         this.dataStore.set("doctors",createdDoctor);
//         console.log(createdDoctor);
//         if (createdDoctor) {
//             this.showMessage(`Created a Doctor!`)
//             this.renderDoctors()
//         } else {
//             this.errorHandler("Error creating! Try again... ");
//         }
    }
    async onDownvote(event) {
        //console.log(event);
        event.preventDefault();
        console.log("in creation");
        console.log(event.currentTarget.myName);
        console.log(event.currentTarget.myDownVotes);
       const updatedVote = await this.client.updateVideoGameDownvote(event.currentTarget.myName);
        console.log(updatedVote);
    }
}
const main = async () => {
    const videoGamePage = new VideoGamePage();
    videoGamePage.mount();
};
window.addEventListener('DOMContentLoaded', main);