package com.driver;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spotify")
public class SpotifyController {

    //Autowire will not work in this case, no need to change this and add autowire
    SpotifyService spotifyService = new SpotifyService();

    @PostMapping("/add-user")
    public String createUser(@RequestParam(name = "name") String name, @RequestParam("mobile") String mobile){
        //create the user with given name and number
        spotifyService.createUser(name,mobile);
        return "Success";
    }

    @PostMapping("/add-artist")
    public String createArtist(@RequestParam(name = "name") String name){
        //create the artist with given name

        spotifyService.createArtist(name);

        return "Success";
    }

    @PostMapping("/add-album")
    public String createAlbum(@RequestParam(name = "title") String title, String artistName){
        //If the artist does not exist, first create an artist with given name
        //Create an album with given title and artist
        spotifyService.createAlbum(title,artistName);
        return "Success";
    }

    @PostMapping("/add-song")
    public Song createSong(@RequestParam("title") String title, @RequestParam("albumName") String albumName,@RequestParam("length") int length) throws Exception{
        //If the album does not exist in database, throw "Album does not exist" exception
        //Create and add the song to respective album
        System.out.println("title is "+title);
        System.out.println("title is "+albumName);
       return  spotifyService.createSong(title,albumName,length);

//        return "Success";
    }

    @PostMapping("/add-playlist-on-length")
    public String createPlaylistOnLength(@RequestParam("mobile") String mobile, @RequestParam("title") String title,@RequestParam("length") int length) throws Exception{
        //Create a playlist with given title and add all songs having the given length in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        try {
            spotifyService.createPlaylistOnLength(mobile, title, length);
        }
        catch(Exception e){
            return e.getMessage();
        }

        return "Success";
    }

    @PostMapping("/add-playlist-on-name")
    public String createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception{
        //Create a playlist with given title and add all songs having the given titles in the database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        try {
            spotifyService.createPlaylistOnName(mobile, title, songTitles);
        }
        catch(Exception e){
            return e.getMessage();
        }


        return "Success";
    }

    @PutMapping("/find-playlist")
    public String findPlaylist(String mobile, String playlistTitle) throws Exception{
        //Find the playlist with given title and add user as listener of that playlist and update user accordingly
        //If the user is creater or already a listener, do nothing
        //If the user does not exist, throw "User does not exist" exception
        //If the playslist does not exists, throw "Playlist does not exist" exception
        // Return the playlist after updating

        spotifyService.findPlaylist(mobile,playlistTitle);


        return "Success";
    }

    @PutMapping("/like-song")
    public String likeSong(@RequestParam("mobile") String mobile, @RequestParam("songTitle") String songTitle) throws Exception{
        //The user likes the given song. The corresponding artist of the song gets auto-liked
        //A song can be liked by a user only once. If a user tried to like a song multiple times, do nothing
        //However, an artist can indirectly have multiple likes from a user, if the user has liked multiple songs of that artist.
        //If the user does not exist, throw "User does not exist" exception
        //If the song does not exist, throw "Song does not exist" exception
        //Return the song after updating

        spotifyService.likeSong(mobile,songTitle);

        return "Success";
    }

    @GetMapping("/popular-artist")
    public Artist mostPopularArtist(){
        //Return the artist name with maximum likes
        return spotifyService.mostPopularArtist();
    }

    @GetMapping("/popular-song")
    public Song mostPopularSong(){
        //return the song title with maximum likes
         return spotifyService.mostPopularSong();
    }
}
