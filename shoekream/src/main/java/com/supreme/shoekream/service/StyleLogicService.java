package com.supreme.shoekream.service;

import com.supreme.shoekream.model.dto.MemberDTO;
import com.supreme.shoekream.model.dto.socialDTO.BoardDTO;
import com.supreme.shoekream.model.dto.socialDTO.FollowDTO;
import com.supreme.shoekream.model.dto.socialDTO.LkDTO;
import com.supreme.shoekream.model.dto.socialDTO.ReplyDTO;
import com.supreme.shoekream.model.entity.*;
import com.supreme.shoekream.model.network.Header;
import com.supreme.shoekream.model.network.request.ReplyApiRequest;
import com.supreme.shoekream.model.network.response.BoardWithLikeListResponse;
import com.supreme.shoekream.model.network.security.KreamPrincipal;
import com.supreme.shoekream.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class StyleLogicService {
    private final BoardRepository boardRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;

    @Transactional(readOnly=true)
    public List<BoardDTO> list(){
//        System.out.println(boardRepository.findAll());
        return BoardDTO.fromEntity(boardRepository.findAll());
    }

    @Transactional(readOnly = true)

    public List<BoardWithLikeListResponse> unlog_trend(){
        List<BoardWithLikeListResponse> trend = BoardWithLikeListResponse.fromEntity(boardRepository.findAll());
        for(int i=0;i<trend.size()-1;i++){
            for (int j=i+1; j<trend.size();j++){
                if(trend.get(i).lks().size() + trend.get(i).replies().size() < trend.get(j).lks().size() + trend.get(j).replies().size()){
                    BoardWithLikeListResponse tmp = trend.get(i);
                    trend.set(i, trend.get(j));
                    trend.set(j, tmp);
                }
            }
        }
        return trend;
    }

    @Transactional(readOnly = true)
    public List<BoardWithLikeListResponse> unlog_newest(){
        List<BoardWithLikeListResponse> newest = BoardWithLikeListResponse.fromEntity(boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        return newest;
    }
    @Transactional(readOnly = true)
    public List<BoardWithLikeListResponse> trendList(MemberDTO memberDTO){
        List<BoardWithLikeListResponse> trend = BoardWithLikeListResponse.fromEntity(boardRepository.findAll());
        for(int i=0;i<trend.size()-1;i++){
            for (int j=i+1; j<trend.size();j++){
                if(trend.get(i).lks().size() + trend.get(i).replies().size() < trend.get(j).lks().size() + trend.get(j).replies().size()){
                    BoardWithLikeListResponse tmp = trend.get(i);
                    trend.set(i, trend.get(j));
                    trend.set(j, tmp);
                }
            }
        }

        List<Lk> lks = likeRepository.findAllByMember(memberDTO.toEntity());

        for(int i=0;i<trend.size();i++){
            for(int j=0;j<lks.size();j++){
                if(lks.get(j).getBoard().getIdx() == trend.get(i).idx()){
                    trend.set(i,
                            BoardWithLikeListResponse.of(trend.get(i).idx(),
                                    trend.get(i).memberDTO(),
                                    trend.get(i).content(),
                                    trend.get(i).img(), trend.get(i).hashtag(),trend.get(i).lks(), trend.get(i).replies(),
                                    trend.get(i).tags(), trend.get(i).createdAt(), trend.get(i).modifiedAt(), true)
                    );

                }
            }
        }
        return trend;
    }


    @Transactional(readOnly = true)
    public List<BoardWithLikeListResponse> newest(MemberDTO memberDTO) {
        List<BoardWithLikeListResponse> newest = BoardWithLikeListResponse.fromEntity(boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        List<Lk> lks = likeRepository.findAllByMember(memberDTO.toEntity());
        for(int i=0;i<newest.size();i++){
            for(int j=0;j<lks.size();j++){
                if(lks.get(j).getBoard().getIdx() == newest.get(i).idx()){
                    newest.set(i,
                            BoardWithLikeListResponse.of(newest.get(i).idx(),
                                    newest.get(i).memberDTO(),
                                    newest.get(i).content(),
                                    newest.get(i).img(), newest.get(i).hashtag(),newest.get(i).lks(), newest.get(i).replies(),
                                    newest.get(i).tags(), newest.get(i).createdAt(), newest.get(i).modifiedAt(), true)
                    );

                }
            }
        }
        return newest;
    }

    public Board read(Long idx){
        Board board = boardRepository.findByIdx(idx);
        return board;
    }

    public Header delete(Long idx){
        Optional<Board> board = boardRepository.findById(idx);
        return board.map(bd ->{
            boardRepository.delete(bd);
            return Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    public Header comment_delete(Long replyIdx){
        Optional<Reply> reply = replyRepository.findById(replyIdx);
        return reply.map(rp -> {
            replyRepository.delete(rp);
            return
                     Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    public List<BoardWithLikeListResponse> getFollowingFeeds(Long idx){
        List<Follow> followings = followRepository.findAllByFollowerIdx(idx);
        List<BoardWithLikeListResponse> feed;
        feed = new ArrayList<>();
        for(int i=0; i<followings.size(); i++){
            List<BoardWithLikeListResponse> sub =BoardWithLikeListResponse.fromEntity( boardRepository.findAllByMemberIdx(followings.get(i).getFollowingIdx()));
            for(int j=0; j<sub.size();j++){
                feed.add(sub.get(j));
            }
        }
        Member member = memberRepository.getReferenceById(idx);
        List<Lk> lks = likeRepository.findAllByMember(member);
        for(int i=0;i<feed.size();i++){
            for(int j=0;j<lks.size();j++){
                if(lks.get(j).getBoard().getIdx() == feed.get(i).idx()){
                    feed.set(i,
                            BoardWithLikeListResponse.of(feed.get(i).idx(),
                                    feed.get(i).memberDTO(),
                                    feed.get(i).content(),
                                    feed.get(i).img(), feed.get(i).hashtag(),feed.get(i).lks(), feed.get(i).replies(),
                                    feed.get(i).tags(), feed.get(i).createdAt(), feed.get(i).modifiedAt(), true)
                    );

                }
            }
        }
        return feed;
    }

    public MemberDTO getMember(Long idx){
        MemberDTO sessionUser = MemberDTO.fromEntity(memberRepository.getReferenceById(idx));
        return sessionUser;
    }

    public MemberDTO getMember(String memberEmail){
        System.out.println("이메일"+memberEmail);
        MemberDTO sessionUser = MemberDTO.fromEntity(memberRepository.findByEmail(memberEmail).get());
        return sessionUser;
    }

    public List<MemberDTO> getLikeMembers(Long boardIdx){
        List<LkDTO> likes = LkDTO.fromEntity(likeRepository.findAllByBoardIdx(boardIdx));
        List<MemberDTO> members = new ArrayList<>();
        for (int i = 0; i < likes.size(); i++){
            members.add(likes.get(i).memberDTO());
        }
        return members;
    }

    public Header<ReplyDTO> createReply(ReplyDTO replyDTO){
//        System.out.println("============서비스============"+replyDTO);  // ✔
        Member member = memberRepository.getReferenceById(replyDTO.memberDTO().idx());
        Board board = boardRepository.getReferenceById(replyDTO.boardIdx());
        Reply newR = replyRepository.save(replyDTO.toEntity(member, board));
//        System.out.println("저장 완료!!!!!!!!!!!!!!!!!");
        ReplyDTO response = ReplyDTO.fromEntity(newR);
        return Header.OK(response);
    }

    public List<BoardDTO> findTopSevenStylePick(){
        List<Board> bds = boardRepository.findAll();
        List<BoardDTO> boards = BoardDTO.fromEntity(bds);
        for (int i=0;i<boards.size()-1;i++){
            for(int j=1;j<boards.size();j++){
                if(boards.get(i).lks().size() > boards.get(j).lks().size()){
                    BoardDTO temp = BoardDTO.of(
                            boards.get(i).idx(),
                            boards.get(i).memberDTO(),
                            boards.get(i).content(),
                            boards.get(i).hashtag(),
                            boards.get(i).img(),
                            boards.get(i).lks(),
                            boards.get(i).replies(),
                            boards.get(i).tags(),
                            boards.get(i).createdAt(),
                            boards.get(i).modifiedAt()
                            );
                    boards.set(i, boards.get(j));
                    boards.set(j, temp);
                }
            }
        }
        List<BoardDTO> response = new ArrayList<>();
        for(int i=0;i<7;i++){
            response.add(boards.get(i));
        }

        return response;
    }

    public List<String> trendHashtags(){
        List<Board> boards = boardRepository.findAll();
        List<Integer> hashCnt = new ArrayList<>();
        List<String> hashtags = new ArrayList<>();
        for(int i=0; i<boards.size(); i++){
            hashtags.add(boards.get(i).getHashtag());
            hashCnt.add(1);
            for(int j=0;j<i;j++){
                if(hashtags.get(i).equals(hashtags.get(j))){
                    hashCnt.set(j, hashCnt.get(j)+1);
                    hashCnt.set(i, 0);
                    break;
                }
            }
        }
        System.out.println(hashtags);
        System.out.println(hashCnt);

        for(int i = 0; i < hashtags.size()-1; i++){
            for(int j = i+1; j < hashtags.size(); j++){
                if(hashCnt.get(i)<hashCnt.get(j)){
                    String temp = hashtags.get(i);
                    int tmp = hashCnt.get(i);
                    hashtags.set(i, hashtags.get(j));
                    hashtags.set(j, temp);

                    hashCnt.set(i, hashCnt.get(j));
                    hashCnt.set(j, tmp);
                }
            }
        }
        List<String> trends = new ArrayList<>();
        for(int i=0;i<5;i++){
            trends.add(hashtags.get(i));
        }
        return trends;
    }

    public List<BoardDTO> isBoardExist(Long memberIdx){
        System.out.println("테스트"+boardRepository.countAllByMemberIdx(memberIdx));
        if(boardRepository.countAllByMemberIdx(memberIdx) > 0){
            return BoardDTO.fromEntity(boardRepository.findAllByMemberIdx(memberIdx));
        }else{
            return null;
        }
    }

    public void like(Long boardIdx, KreamPrincipal kreamPrincipal){
        Lk like = new Lk();
        like.setBoard(boardRepository.findByIdx(boardIdx));
        like.setMember(kreamPrincipal.toFullDto().toEntity());
        likeRepository.save(like);
    }

    public void unlike(Long boardIdx, KreamPrincipal kreamPrincipal){
        Lk lk = likeRepository.findByBoardAndMember(boardRepository.findById(boardIdx).get(), kreamPrincipal.toFullDto().toEntity());
        likeRepository.delete(lk);
    }

public List<FollowDTO> countFollowers(Long memberIdx){//내가 팔로우하고 있는 사람들 뽑기
         List<Follow> follows = followRepository.findAllByFollowingIdx(memberIdx);
         List<FollowDTO> followDTOS = new ArrayList<>();
         for(int i=0;i<follows.size();i++){
             followDTOS.add(FollowDTO.fromEntity(follows.get(i),
                     MemberDTO.fromEntity(memberRepository.getReferenceById(follows.get(i).getFollowerIdx())),
                     MemberDTO.fromEntity(memberRepository.getReferenceById(follows.get(i).getFollowingIdx()))));
         }
         return followDTOS;
    }

    public List<FollowDTO> countFollowing(Long memberIdx){//나를 팔로우하고 있는 사람들 뽑기
        List<Follow> followers = followRepository.findAllByFollowerIdx(memberIdx);
        List<FollowDTO> followerDTOS = new ArrayList<>();
        for(int i=0;i<followers.size();i++){
            followerDTOS.add(FollowDTO.fromEntity(followers.get(i),
                    MemberDTO.fromEntity(memberRepository.getReferenceById(followers.get(i).getFollowerIdx())),
                    MemberDTO.fromEntity(memberRepository.getReferenceById(followers.get(i).getFollowingIdx()))));
        }
        return followerDTOS;
    }

    public List<BoardWithLikeListResponse> getHashtagFeed(String hashtag, MemberDTO memberDTO){
        List<BoardWithLikeListResponse> feed = BoardWithLikeListResponse.fromEntity(boardRepository.findAllByHashtag(hashtag));
        System.out.println("해시태그"+feed);

        List<Lk> lks = likeRepository.findAllByMember(memberDTO.toEntity());
        for(int i=0;i<feed.size();i++){
            for(int j=0;j<lks.size();j++){
                if(lks.get(j).getBoard().getIdx() == feed.get(i).idx()){
                    feed.set(i,
                            BoardWithLikeListResponse.of(feed.get(i).idx(),
                                    feed.get(i).memberDTO(),
                                    feed.get(i).content(),
                                    feed.get(i).img(), feed.get(i).hashtag(),feed.get(i).lks(), feed.get(i).replies(),
                                    feed.get(i).tags(), feed.get(i).createdAt(), feed.get(i).modifiedAt(), true)
                    );

                }
            }
        }
        return feed;
    }

    public List<BoardWithLikeListResponse> getHashtagFeed_unlog(String hashtag){
        List<BoardWithLikeListResponse> feed = BoardWithLikeListResponse.fromEntity(boardRepository.findAllByHashtag(hashtag));
        System.out.println("해시태그"+feed);
        return feed;
    }

    public void follow(Long memberIdx, MemberDTO kreamPrincipal){
        Follow follow = new Follow();
        follow.setFollowerIdx(kreamPrincipal.idx());
        follow.setFollowingIdx(memberIdx);
        followRepository.save(follow);
    }

    public void unfollow(Long memberIdx, MemberDTO kreamPrincipal){
        Follow follow = followRepository.findByFollowerIdxAndFollowingIdx(kreamPrincipal.idx(), memberIdx).get();
        followRepository.delete(follow);
    }
}
