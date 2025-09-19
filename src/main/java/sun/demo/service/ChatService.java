package sun.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.demo.dto.chat.ChatRoomDto;
import sun.demo.dto.chat.CreateChatRoomRequest;
import sun.demo.model.ChatRoom;
import sun.demo.model.ChatRoomMember;
import sun.demo.model.MemberRole;
import sun.demo.model.Users;
import sun.demo.repository.ChatRoomMemberRepository;
import sun.demo.repository.ChatRoomRepository;
import sun.demo.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;



    public ChatRoomDto createChatRoom(CreateChatRoomRequest request, Long createdBy){

        Users creator = userRepository.findById(createdBy).orElseThrow();

        //ChatRoom
        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.name())
                .description(request.description())
                .type(request.type())
                .imageUrl(request.imageUrl())
                .maxMembers(request.maxMembers())
                .createdBy(creator)
                .build();

        ChatRoom saveRoom = chatRoomRepository.save(chatRoom);

        //chatroommember

        ChatRoomMember ownerMember = ChatRoomMember.builder()
                .chatRoom(saveRoom)
                .user(creator)
                .role(MemberRole.OWNER)
                .build();

        chatRoomMemberRepository.save(ownerMember);


        //todo : 세션 갱신

    }
}
