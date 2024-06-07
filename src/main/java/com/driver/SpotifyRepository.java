package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
           User user = new User(); // creating new user object
           user.setName(name);  // setting name and mobile no
           user.setMobile(mobile);
           users.add(user);  // adding in the list of users

           return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);;
        artists.add(artist);

        return artist;
    }

    public Album createAlbum(String title, String artistName) {
       Album album = new Album();
       album.setTitle(title);
       albums.add(album);
       if(!artists.contains(artistName)){
           Artist artist = new Artist();
           artist.setName(artistName);
       }
       return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song();
        song.setTitle(title);
        song.setLength(length);

        if(!albums.contains(albumName)){
            Album album = new Album();
            albums.add(album);
        }

        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
         Playlist playlist = new Playlist(title);
         playlists.add(playlist);

         for(Song song: songs){
             int len = song.getLength();
             if(len==length){
                 if(playlistSongMap.containsKey(playlist)){
                     List<Song> tempSong = playlistSongMap.get(playlist);
                      tempSong.add(song);
                      playlistSongMap.put(playlist,tempSong);
                 }
                 else {
                     List<Song> tempSong = new ArrayList<>();
                     tempSong.add(song);
                     playlistSongMap.put(playlist, tempSong);
                 }
             }
         }
         boolean flag = false;
         for(User user: users){
             if(user.getMobile()==mobile){
                 flag = true;
                 creatorPlaylistMap.put(user,playlist);

                 if(userPlaylistMap.containsKey(playlist)){
                     List<Playlist> tempPlaylist = userPlaylistMap.get(playlist);
                     tempPlaylist.add(playlist);
                     userPlaylistMap.put(user,tempPlaylist);
                 }
                 else{
                     List<Playlist> tempPlaylist = new ArrayList<>();
                     tempPlaylist.add(playlist);
                     userPlaylistMap.put(user,tempPlaylist);
                 }
                 if(!playlistListenerMap.containsKey(playlist)){
                     List<User> tempUser = new ArrayList<>();
                     tempUser.add(user);
                     playlistListenerMap.put(playlist,tempUser);
                 }
                 else{
                     List<User> tempUser = playlistListenerMap.get(playlist);
                     tempUser.add(user);
                     playlistListenerMap.put(playlist,tempUser);
                 }
                 break;
             }
         }
         if(flag==false){
             throw new RuntimeException("user does not exist");
         }

         return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);

        for(String song : songTitles){
            if(playlistSongMap.containsKey(playlist)){
                List<Song> tempSongs = playlistSongMap.get(playlist);
                Song newSong = new Song();
                newSong.setTitle(song);
                tempSongs.add(newSong);
                playlistSongMap.put(playlist,tempSongs);
            }
            else{
                List<Song> tempSongs= new ArrayList<>();
                Song newSong = new Song();
                newSong.setTitle(song);
                tempSongs.add(newSong);
                playlistSongMap.put(playlist,tempSongs);
            }
        }
        boolean flag = false;
        for(User user: users){
            if(user.getMobile()==mobile){
                flag = true;
                creatorPlaylistMap.put(user,playlist);
                if(userPlaylistMap.containsKey(playlist)){
                    List<Playlist> tempPlaylist = userPlaylistMap.get(playlist);
                    tempPlaylist.add(playlist);
                    userPlaylistMap.put(user,tempPlaylist);
                }
                else{
                    List<Playlist> tempPlaylist = new ArrayList<>();
                    tempPlaylist.add(playlist);
                    userPlaylistMap.put(user,tempPlaylist);
                }
                if(playlistListenerMap.containsKey(playlist)){
                    List<User> tempUser = playlistListenerMap.get(playlist);
                    tempUser.add(user);
                    playlistListenerMap.put(playlist,tempUser);
                }
                else{
                    List<User> tempUser = new ArrayList<>();
                    tempUser.add(user);
                    playlistListenerMap.put(playlist,tempUser);
                }
                break;
            }
        }
        if(flag==false){
            throw new RuntimeException("user does not exist");
        }
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist playlist = new Playlist(playlistTitle);
        if(!playlists.contains(playlist)){
           throw new RuntimeException("Playlist does not exist");
        }
        boolean userExist = false;
        for(User user: users){
            if(user.getMobile()==mobile){
                userExist = true;
                if(!creatorPlaylistMap.containsKey(user) && creatorPlaylistMap.get(user)!=playlist){
                    if(playlistListenerMap.containsKey(playlist)){
                        List<User> tempUser = playlistListenerMap.get(playlist);
                        tempUser.add(user);
                        playlistListenerMap.put(playlist,tempUser);
                    }
                    else{
                        List<User> tempUser = new ArrayList<>();
                        tempUser.add(user);
                        playlistListenerMap.put(playlist,tempUser);
                    }
                }
            }
        }
        if(userExist==false){
            throw new RuntimeException("User does not exist");
        }
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song song = new Song();
        boolean songExist = false;

        for(Song ele : songs){
            if(ele.getTitle()==songTitle){
                songExist = true;
                song = ele;
                break;
            }
        }

        if(songExist==false){
            throw new RuntimeException("Song does not exixt");
        }
         boolean userExist = false;
         for(User user: users){
             if(user.getMobile()==mobile) {
                 userExist = true;
                 song.setLikes(song.getLikes() + 1);
             }
         }
         return song;
    }

    public Artist mostPopularArtist() {
        Artist artist = new Artist();
        int mxLikes = Integer.MIN_VALUE;
        for(Artist artistEle : artists){
            if(artist.getLikes()>mxLikes){
                mxLikes = artist.getLikes();
                artist = artistEle;
            }
        }
        return artist;
    }

    public Song mostPopularSong() {
       Song song = new Song();
       int mxLikes = Integer.MIN_VALUE;
       for(Song ele : songs){
           if(ele.getLikes()>mxLikes){
               song = ele;
               mxLikes = ele.getLikes();
           }
       }
       return song;
    }
}
