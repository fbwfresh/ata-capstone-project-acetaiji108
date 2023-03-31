import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VideoGameClient from "../api/videoGameClient";
import ExampleClient from "../api/exampleClient";

class Top5Page extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['getTop5', 'renderTop5'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.client = new VideoGameClient();
        this.dataStore.addChangeListener(this.renderTop5)
        this.getTop5();
    }

    async renderTop5() {
        let firstSlide = document.getElementById("firstSlide");
        let secondSlide = document.getElementById("secondSlide");
        let thirdSlide = document.getElementById("thirdSlide");
        let fourthSlide = document.getElementById("fourthSlide");
        let fifthSlide = document.getElementById("fifthSlide");
        let top5results = document.getElementById("top5Games");
        top5results.innerHTML = "<h1>Leaderboard</h1>";
        const slides = [firstSlide, secondSlide, thirdSlide, fourthSlide, fifthSlide];
        const top5 = this.dataStore.get("top5");
        let i = 0;
        let x = 1;
        if (top5) {
            for (const game of top5) {
                slides[i].innerHTML = `<img src=${game.image}>`
                i++;
                top5results.innerHTML += `<div><h2>${x}. ${game.name}</h2></div>`
                x++;
               // top5results.innerHTML += `<div ><img class="rounded" src=${game.image} width="500" height="500"></div>`
            }
        }
    }

    async getTop5(event) {
        // Prevent the page from refreshing on form submit
        //event.preventDefault();
        this.dataStore.set("top5",null);
        let result = await this.client.getTop5(this.errorHandler);
        this.dataStore.set("top5",result);
        console.log(result);
        // this.renderTop5();

    }

}
const main = async () => {
    const top5Page = new Top5Page();
    await top5Page.mount();
};

window.addEventListener('DOMContentLoaded', main);
