import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class VideoGameClient extends BaseClass {
    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'createVideoGame', 'getVideoGame', 'deleteVideoGame','getAllVideoGames','updateVideoGameUpvote'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    async getVideoGame(name, errorCallback) {
        try {
            const response = await this.client.get(`/games/${name}`);
            return response.data;
        } catch (error) {
            this.handleError("getVideoGame", error, errorCallback)
        }
    }
    async getAllVideoGames(errorCallback) {
        try {
            const response = await this.client.get(`/games/all`);
            console.log(response.data);
            return response.data;
        } catch (error) {
            this.handleError("getAllVideoGames", error, errorCallback)
        }
    }

    async createVideoGame(name,description,image,consoles, errorCallback) {
        try {
            const response = await this.client.post(`games`, {
                //I put it in lowercase for description and consoles but on dynamo they are upper case
                "name": name,
                "description": description,
                "image": image,
                "consoles": consoles
            });
            console.log(response.data);
            return response.data;
        } catch (error) {
            this.handleError("createVideoGame", error, errorCallback);
        }
    }

    async deleteVideoGame(name,errorCallback){
        try{
            const response = await this.client.delete(`games/${name}`);
            console.log(response.data);
            return response.data;
        }catch (error){
            this.handleError("deleteVideoGame",error, errorCallback)
        }
    }

    async updateVideoGameUpvote(name,errorCallback){
        try{
            const response = await this.client.put(`games/${name}/upvote`);
            return response.data;
        }catch (error){
            this.handleError("updateVideoGameUpvote",error, errorCallback)
        }
    }
    async updateVideoGameDownvote(name,errorCallback){
        try{
            const response = await this.client.put(`games/${name}/downvote`);
            return response.data;
        }catch (error){
            this.handleError("updateVideoGameDownvote",error, errorCallback)
        }
    }

    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}