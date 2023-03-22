import BaseClass from "../util/baseClass";
import axios from 'axios'

export default class VideoGameClient extends BaseClass {
    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'createVideoGame', 'getVideoGame', 'deleteVideoGame'];
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

    async createVideoGame(name, errorCallback) {
        try {
            const response = await this.client.post(`games`, {
                //I put it in lowercase for description and consoles but on dynamo they are upper case
                "name": name,
                "description": description,
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