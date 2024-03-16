const JoinBtn = document.querySelector(".Join_btn");
const JoinForm = document.querySelector(".join_Form");
const confirmDuplication = document.querySelector(".confirmDuplication");
const email = document.querySelector(".email");
const formData = new FormData();

email.addEventListener('input', function() { // input을 눌려서 값을 변경할 때 발생하는 이벤트
    if(confirmDuplication.classList.contains("confirmed")) {
        confirmDuplication.classList.remove("confirmed");
    }
})

confirmDuplication.addEventListener("click", function (e) {
    e.preventDefault();

    if (JoinForm['email'].value === "") {
        alert("이메일 중복 확인을 위해 이메일을 입력하세요")
        return;
    }

    axios.get("/confirmEmail", {params : {email: JoinForm['email'].value}})
        .then(res => {
            if (res.data === "FAILURE_DUPLICATED_USER_EMAIL") {
                alert("이미 존재하는 회원입니다.");

            } else if(res.data === "SUCCESS"){
                confirmDuplication.classList.add("confirmed")
                alert("사용가능한 이메일입니다.");
            }
        })
        .catch(err => {
            console.log(err);
        })
})

JoinBtn.addEventListener("click", function (e) {
    e.preventDefault();

    const USERNAME_REGEX = /^[a-zA-Z](?=.*[a-zA-Z])(?=.*[0-9]).{4,12}$/g; // 최소4글자 최대 12글자 영문필수 숫자필수
    const PASSWORD_REGEX = /^(?=.*[A-Za-z\d@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const EMAIL_REGEX = /^(?:[a-z0-9!#$%&amp;'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&amp;'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/;

    if (JoinForm['username'].value === "") {
        alert("아이디를 입력하세요");
        return;
    }
    if (!USERNAME_REGEX.test(JoinForm['username'].value)) {
        alert("아이디는 영문으로 최소4글자 최대 12글자 숫자포함이 되어야합니다.");
        return;
    }

    if (JoinForm['password'].value === "") {
        alert("비밀번호를 입력하세요")
        return;
    }

    if (!PASSWORD_REGEX.test(JoinForm['password'].value)) {
        alert("비밀번호는 8자이상 1개 이상의 특수문자로 이루어져야합니다");
        return;
    }

    if (JoinForm['email'].value === "") {
        alert("이메일을 입력하세요")
        return;
    }

    if (!EMAIL_REGEX.test(JoinForm['email'].value)) {
        alert("이메일 형식으로 작성하세요");
        return;
    }
    if(!confirmDuplication.classList.contains("confirmed")){
        alert("이메일 중복 확인을 진행해 주세요");
        return;
    }

    formData.append("username", JoinForm['username'].value);
    formData.append("password", JoinForm['password'].value);
    formData.append("email", JoinForm['email'].value);

    axios.post("/joinForm", formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            if(res.data === "SUCCESS"){
                alert("회원가입이 완료되었습니다.");
                location.href = "/loginForm";
            }
        })
        .catch(err => {
            console.log(err);
        })
})