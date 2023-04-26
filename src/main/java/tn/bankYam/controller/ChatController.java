package tn.bankYam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.HttpResource;
import tn.bankYam.dto.*;
import tn.bankYam.service.ChatroomService;
import tn.bankYam.service.FriendsService;
import tn.bankYam.utils.ScriptUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("chat")
public class ChatController {
	@Autowired
	private ChatroomService chatroomService;
	@Autowired
	private FriendsService friendsService;
	@Value("${file.dir.upload}")
	private String fileDir;

	@GetMapping("list")
	public String chatList(Model model, HttpSession session, HttpServletResponse response) throws IOException {
		Membery membery = (Membery)session.getAttribute("membery");
		model.addAttribute("chatroomList", chatroomService.selectChatRoomS(membery.getMb_seq()));
		model.addAttribute("frList", friendsService.selectFrList(membery));
		//ScriptUtil.newWindow(response, "room?cr_seq="+2);
		return "chatList";
	}

	@GetMapping("room")
	public String chatRoom(long cr_seq, Model model, HttpSession session){
		Membery membery = (Membery)session.getAttribute("membery");
		List<Friend> friendList = friendsService.selectFrList(membery);
		List<Friend> removeFriendList = new ArrayList<>();
		Chatroom chatroom = chatroomService.selectRoomS(cr_seq);
		System.out.println(friendList);
		for(Friend friend : friendList){
			for (Membery chatMember : chatroom.getMemberyList()) {
				if (friend.getF_f_mb_seq() == chatMember.getMb_seq())
					removeFriendList.add(friend);
			}
		}
		for(Friend removeFriend : removeFriendList){
			friendList.remove(removeFriend);
		}
		model.addAttribute("friendList", friendList);
		model.addAttribute("roomInfo", chatroom);
		model.addAttribute("contents", chatroomService.selectContentS(cr_seq));
		model.addAttribute("files", chatroomService.selectFilesS(cr_seq));
		return "chatRoom";
	}

	@GetMapping("insert")
	@ResponseBody
	public long insertRoom(@RequestParam(value = "f_mb_seq[]") ArrayList<Long> f_mb_seq, HttpSession session, HttpServletResponse response) throws IOException{
		Membery membery = (Membery) session.getAttribute("membery");
		if(f_mb_seq.size() == 1){
			Chatmember chatmember= chatroomService.checkRoomS(membery.getMb_seq(), f_mb_seq.get(0));
			if(chatmember != null){
				return chatmember.getCm_cr_seq();
			}
		}
		//채팅방 만들고 채팅인원 인서트 한번에 서비스 합시다.
		long roomNumber = chatroomService.makeRoomS(membery, f_mb_seq);
		return roomNumber;
	}

	@RequestMapping("/mychatt")
	@ResponseBody
	public ModelAndView chatt() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("chatting");
		return mv;
	}

	@GetMapping("readContent")
	@ResponseBody
	public List<Chatcontent> readContent(long cr_seq, HttpSession session){
		long mb_seq = ((Membery)session.getAttribute("membery")).getMb_seq();
		chatroomService.deleteStatusS(mb_seq, cr_seq);
		return chatroomService.selectStatusCount(cr_seq);
	}

	@GetMapping("addChatMember")
	@ResponseBody
	public long addChatMember(long cr_seq, long f_f_mb_seq, HttpSession session){
		Membery membery = (Membery) session.getAttribute("membery");
		Chatroom chatroom = chatroomService.selectRoomS(cr_seq);
		List<Long> f_f_mb_seqList = new ArrayList<>();
		for(Membery chatMembery : chatroom.getMemberyList()){
			if(chatMembery.getMb_seq() == f_f_mb_seq){
				return 0;
			}
			if(chatMembery.getMb_seq() != membery.getMb_seq())
				f_f_mb_seqList.add(chatMembery.getMb_seq());
		}

		//채팅방이 두명일때 채팅방을 하나 더 만들고 창도 띄우기
		if(chatroom.getMemberyList().size() == 2){
			f_f_mb_seqList.add(f_f_mb_seq);
			long roomNumber = chatroomService.makeRoomS(membery, f_f_mb_seqList);
			return roomNumber;
		}else{//채팅방이 세명일때 채팅방에 초대하기
			Chatmember chatmember = new Chatmember();
			chatmember.setCm_cr_seq(cr_seq);
			chatmember.setCm_mb_seq(f_f_mb_seq);
			chatroomService.insertMemberS(chatmember);
			return cr_seq;
		}
	}

	@PostMapping("/upload")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> map)throws IOException{
		for(String key : map.keySet()){
			System.out.println(key);
			System.out.println(map.get(key));
		}
		if (file.isEmpty()) {
			return null;
		}
		String origName = file.getOriginalFilename(); // 원래 파일 이름 추출
		String uuid = UUID.randomUUID().toString(); // 파일 이름으로 쓸 uuid 생성
		String extension = origName.substring(origName.lastIndexOf(".")); // 확장자 추출(ex : .png)
		String savedName = uuid + extension; // uuid와 확장자 결합
		String savedPath = fileDir + savedName; // 파일을 불러올 때 사용할 파일 경로

		// 파일 경로 생성
		Path uploadPath = Paths.get(fileDir, savedName);
		// 파일 저장
		Files.write(uploadPath, file.getBytes());

		//file.transferTo(new File(savedPath));
		savedPath=savedPath.substring(savedPath.lastIndexOf("/file"));


		file.transferTo(new File(savedPath)); // 실제로 로컬에 uuid를 파일명으로 저장

		return "11";
	}
}
