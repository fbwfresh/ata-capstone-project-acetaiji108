import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VideoGameClient from "../api/videoGameClient";

class VideoGamePage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onFindByName', 'renderVideoGames', 'renderByVideoGameName','onCreate'], this);
        this.dataStore = new DataStore();
    }

    mount() {
        const MonsterHunterUpvote = document.getElementById("MonsterHunterUpvote");
        MonsterHunterUpvote.addEventListener('click',this.onCreate,false);
        MonsterHunterUpvote.myName = "MonsterHunterID";
        MonsterHunterUpvote.myUpvotes = 0;
        document.getElementById('upvote').addEventListener('click', this.onCreate);
        document.getElementById('downvote').addEventListener('click', this.onFindById);
        document.getElementById('upvote').addEventListener('click', this.onCreate);
        document.getElementById('downvote').addEventListener('click', this.onFindById);
        document.getElementById('upvote').addEventListener('click', this.onCreate);
        document.getElementById('downvote').addEventListener('click', this.onFindById);
        document.getElementById('upvote').addEventListener('click', this.onCreate);
        document.getElementById('downvote').addEventListener('click', this.onFindById);
        document.getElementById('upvote').addEventListener('click', this.onCreate);
        document.getElementById('downvote').addEventListener('click', this.onFindById);
        this.client = new VideoGameClient();
        this.dataStore.addChangeListener(this.renderVideoGames())
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
        const table = document.getElementById("result-info");
        const doctors = this.dataStore.get("doctors");
        table.innerHTML += `
                          <div><td>${doctors.doctorId}</td> </div>
                           <div><td>${doctors.name}</td></div>
                           <div><td>${doctors.dob}</td></div>
                           <div><td>${doctors.isActive}</td></div>
                               `
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

    async onCreate(event){
        //console.log(event);
        event.preventDefault();
        // event.stopImmediatePropagation();

        const videoGameElement = dataStore.get(event.currentTarget.myName);
        // event.currentTarget.myUpvotes = event.currentTarget.myUpvotes + 1;

        let name = document.getElementById("add-doctor-name-field").value;
        let dob = document.getElementById("add-doctor-dob-field").value;
//this is where the doctor gets created on the page by inputting the information we saved into variables
        const createdDoctor = await this.client.createDoctor(name, dob, this.errorHandler);
        //Setting updating the value in doctors to be the new created doctor
        this.dataStore.set("doctors",createdDoctor);
        console.log(createdDoctor);
        if (createdDoctor) {
            this.showMessage(`Created a Doctor!`)
            this.renderDoctors()
        } else {
            this.errorHandler("Error creating! Try again... ");
        }
    }
}