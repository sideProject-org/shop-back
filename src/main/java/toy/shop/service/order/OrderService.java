package toy.shop.service.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.shop.cmmn.exception.NotFoundException;
import toy.shop.domain.item.Item;
import toy.shop.domain.member.Address;
import toy.shop.domain.member.Member;
import toy.shop.domain.order.Order;
import toy.shop.domain.order.OrderDetail;
import toy.shop.dto.order.OrderDetailsSaveRequestDTO;
import toy.shop.dto.order.OrderSaveRequestDTO;
import toy.shop.jwt.UserDetailsImpl;
import toy.shop.repository.item.ItemRepository;
import toy.shop.repository.member.AddressRepository;
import toy.shop.repository.member.MemberRepository;
import toy.shop.repository.order.OrderDetailRepository;
import toy.shop.repository.order.OrderRepository;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ItemRepository itemRepository;

    private static final String IMP_URL = "https://api.iamport.kr/payments/cancel";

    /**
     * 결제 메서드.
     *
     * 주어진 회원, 배송지, 아이템 상세 정보를 기반으로 주문 DB를 생성합니다.
     * 또한, 상품의 재고 수량을 확인하여, 재고가 부족할 경우 예외를 발생시킵니다.
     *
     * @param parameter    주문 정보를 담고 있는 {@link OrderSaveRequestDTO}.
     * @param userDetails  현재 로그인한 사용자의 정보를 담고 있는 {@link UserDetailsImpl}.
     * @throws UsernameNotFoundException   사용자가 존재하지 않을 경우 발생.
     * @throws NotFoundException           배송지 또는 상품이 존재하지 않을 경우 발생.
     * @throws IllegalArgumentException    상품의 재고가 부족할 경우 발생.
     */
    @Transactional
    public void saveOrder(OrderSaveRequestDTO parameter, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        Address address = addressRepository.findById(parameter.getAddressId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 배송지입니다."));
        Order order = Order.builder()
                .member(member)
                .address(address)
                .orderNumber(parameter.getOrderNumber())
                .paymentMethod(parameter.getPaymentMethod())
                .totalPrice(parameter.getTotalPrice())
                .build();

        Order savedOrder = orderRepository.save(order);

        for (OrderDetailsSaveRequestDTO detail : parameter.getOrderDetails()) {
            // Item 정보 조회
            Item item = itemRepository.findById(detail.getItemId())
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다."));

            boolean bool = item.updateQuantity(detail.getItemQuantity());
            if (!bool) {
                throw new IllegalArgumentException("상품 재고가 부족합니다.");
            }

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(savedOrder)
                    .item(item)
                    .price(detail.getItemPrice())
                    .quantity(detail.getItemQuantity())
                    .build();

            orderDetailRepository.save(orderDetail);
        }
    }

    /**
     * 아임포트(IMP) 환불 요청 메서드.
     *
     * 주어진 액세스 토큰, 주문 고유 ID(merchant_uid), 환불 사유를 사용하여
     * 아임포트(IMP) API를 호출하고 환불 요청을 수행합니다.
     *
     * @param access_token   아임포트 API 호출에 필요한 액세스 토큰.
     * @param merchant_uid   환불을 요청할 주문의 고유 식별자.
     * @param reason         환불 요청 사유.
     * @throws IOException   API 요청 중 네트워크 문제 또는 I/O 오류가 발생할 경우 발생.
     */
    public void refundRequest(String access_token, String merchant_uid, String reason) throws IOException {
        URL url = new URL(IMP_URL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식을 POST로 설정
        conn.setRequestMethod("POST");

        // 요청의 Content-Type, Accept, Authorization 헤더 설정
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", access_token);

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("merchant_uid", merchant_uid);
        json.addProperty("reason", reason);

        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        br.close();
        conn.disconnect();
    }

    /**
     * 아임포트(IMP) 액세스 토큰 요청 메서드.
     *
     * 주어진 API 키와 시크릿 키를 사용하여 아임포트(IMP) API에서 액세스 토큰을 발급받습니다.
     * 발급받은 액세스 토큰은 API 인증에 사용됩니다.
     *
     * @param apiKey      아임포트 API 호출에 필요한 API 키.
     * @param secretKey   아임포트 API 호출에 필요한 시크릿 키.
     * @return            발급받은 액세스 토큰(String).
     * @throws IOException           API 요청 중 네트워크 문제 또는 I/O 오류가 발생할 경우 발생.
     * @throws MalformedURLException URL 형식이 잘못되었을 경우 발생.
     */
    public String getToken(String apiKey, String secretKey) throws IOException, MalformedURLException {
        URL url = new URL(IMP_URL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식을 POST로 설정
        conn.setRequestMethod("POST");

        // 요청의 Content-Type과 Accept 헤더 설정
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("imp_key", apiKey);
        json.addProperty("imp_secret", secretKey);

        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString()); // json 객체를 문자열 형태로 HTTP 요청 본문에 추가
        bw.flush(); // BufferedWriter 비우기
        bw.close(); // BufferedWriter 종료

        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Gson gson = new Gson(); // 응답 데이터를 자바 객체로 변환
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
        br.close(); // BufferedReader 종료

        conn.disconnect(); // 연결 종료

        return accessToken;
    }
}
