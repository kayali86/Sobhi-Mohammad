package com.kayali_developer.sobhimohammad.utilities;

import com.kayali_developer.sobhimohammad.data.model.PlayListItem;
import com.kayali_developer.sobhimohammad.data.model.Video;

public class CustomItemConverter {

    public static Video playListItemToVideo(PlayListItem playListItem){
        Video video = null;
        if (playListItem != null){
            video = new Video();
            video.setLocalVideoId(playListItem.getSnippet().getResourceId().getVideoId());
            video.setId(new Video.Id(playListItem.getSnippet().getPlaylistId(), playListItem.getSnippet().getResourceId().getVideoId()));
            Video.Snippet snippet = new Video.Snippet(
                    playListItem.getSnippet().getPublishedAt(),
                    playListItem.getSnippet().getTitle(),
                    playListItem.getSnippet().getDescription(),
                    playListItem.getSnippet().getThumbnails()
            );
            video.setSnippet(snippet);
        }
        return video;
    }

}
