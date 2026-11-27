package com.test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//Design Twitter (Java)
//Design a simplified version of Twitter where users can post tweets, follow/unfollow another user, and is able to see the 10 most recent tweets in the user's news feed.
//Implement the Twitter class:
//        • Twitter() Initializes your twitter object.
//        • void postTweet(int userId, int tweetId) Composes a new tweet with ID tweetId by the user userId. Each call to this function will be made with a unique tweetId.
//        • List<Integer> getNewsFeed(int userId) Retrieves the 10 most recent tweet IDs in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user themself. Tweets must be ordered from most recent to least recent.
//        • void follow(int followerId, int followeeId) The user with ID followerId started following the user with ID followeeId.
//        • void unfollow(int followerId, int followeeId) The user with ID followerId started unfollowing the user with ID followeeId.
public class Twitter {
    Map<Integer, LinkedHashMap<Integer, Long>> twitterStore = new HashMap<>();

    Map<Integer, Set<Integer>> followStore = new HashMap<>();
    /**
     * Initialize your data structure here.
     */
    public Twitter() {
        // TODO: initialize data structures

    }


    /**
     * Compose a new tweet.
     */
    public void postTweet(int userId, int tweetId) {
        // TODO: implement
        LinkedHashMap<Integer, Long> userTweets = twitterStore.get(userId);
       if(userTweets!=null){
           if(userTweets.get(tweetId)!=null){
               //dedupe
           }else{
               userTweets.put(tweetId,System.nanoTime());
           }
       }else{
           userTweets = new LinkedHashMap<>();
           userTweets.put(tweetId,System.nanoTime());
           twitterStore.put(userId,userTweets);
       }
    }

    /**
     * Retrieve the 10 most recent tweet IDs in the user's news feed.
     * Tweets must be ordered from most recent to least recent.
     */
    public List<Integer> getNewsFeed(int userId) {
        // TODO: implement
        PriorityQueue<Map.Entry<Integer,Long>> pq = new PriorityQueue<>((a,b)->Long.compare(a.getValue(),b.getValue()));
        Set<Integer> followees = followStore.get(userId);

        rankTweets(userId, pq);
        if(followees!=null){
            for (Integer followee : followees) {
                rankTweets(followee, pq);
            }
        }
        List<Integer> list = new ArrayList<>();

        for(int i=0;i<10;i++){
            if(!pq.isEmpty()){
                list.add(pq.poll().getKey());
            }

        }
        return list;
    }

    private void rankTweets(Integer followee, PriorityQueue<Map.Entry<Integer, Long>> pq) {
        LinkedHashMap<Integer, Long> tweets = twitterStore.get(followee);
        //TODO need to replace a solution to avoid iterate all the tweets
        for (Map.Entry<Integer, Long> tweet : tweets.entrySet()) {
            if(pq.size()<10){
                pq.add(tweet);
            }else{
                Long createdTime = tweet.getValue();
                Long oldestTweetInQueue = pq.peek().getValue();
                if(createdTime>oldestTweetInQueue){
                    pq.poll();
                    pq.add(tweet);
                }
            }

        }
    }

    /**
     * Follower follows a followee.
     */
    public void follow(int followerId, int followeeId) {
        // TODO: implement
        if(followStore.get(followerId)==null){
            Set<Integer> set = new HashSet<>();
            set.add(followeeId);
            followStore.put(followerId,set);
        }else{
            followStore.get(followerId).add(followeeId);
        }
    }

    /**
     * Follower unfollows a followee.
     */
    public void unfollow(int followerId, int followeeId) {
        // TODO: implement
        if(followStore.get(followerId)!=null){
            followStore.get(followerId).remove(followeeId);
        }
    }
    // ==========================
    // Simple Test Harness
    // ==========================
    public static void main(String[] args) {
        Twitter twitter = new Twitter();

        System.out.println("postTweet(1, 5)");
        twitter.postTweet(1, 5);

        System.out.println("getNewsFeed(1) -> expected [5]");
        print(twitter.getNewsFeed(1));

        System.out.println("follow(1, 2)");
        twitter.follow(1, 2);

        System.out.println("postTweet(2, 6)");
        twitter.postTweet(2, 6);

        System.out.println("getNewsFeed(1) -> expected [6, 5]");
        print(twitter.getNewsFeed(1));

        System.out.println("unfollow(1, 2)");
        twitter.unfollow(1, 2);

        System.out.println("getNewsFeed(1) -> expected [5]");
        print(twitter.getNewsFeed(1));
    }

    private static void print(List<Integer> feed) {
        System.out.println(feed);
    }
}