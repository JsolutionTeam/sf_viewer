package kr.co.jsol.api.config.jwt;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Jwt {



    @NoArgsConstructor @AllArgsConstructor @Data @Builder
    public static class Payload {
        private String sub;  // 아이디
        private String name; // 이름
        private String role; // 권한
        private String iss; // 작성자
        private Long iat; // 생성일
        private Long exp; // 만료일

        public Payload(Claims body) {
            this.sub = body.get("sub").toString();
            this.name = body.get("name").toString();
            this.role = body.get("role").toString();
            this.iss = body.get("iss").toString();
            this.iat = Long.parseLong(body.get("iat").toString());
            this.exp = Long.parseLong(body.get("exp").toString());
        }

        public boolean isNotJsol(){
            return !isJsol();
        }

        public boolean isJsol(){
            System.out.println("isJsol ? " +this.role.toUpperCase().startsWith("ROLE_JSOL"));
            return this.role.toUpperCase().startsWith("ROLE_JSOL");
        }

        public boolean isNotAgency(){
            return !isAgency();
        }

        public boolean isAgency(){
            System.out.println("isAgency ? " +this.role.toUpperCase().startsWith("ROLE_AGENCY"));
            return this.role.toUpperCase().startsWith("ROLE_AGENCY");
        }

        public boolean isNotStore(){
            return !isStore();
        }

        public boolean isStore(){
            System.out.println("isStore ? " +this.role.toUpperCase().startsWith("ROLE_STORE"));
            return this.role.toUpperCase().startsWith("ROLE_STORE");
        }

        public boolean isNotAgencyOwner(){
            return !isAgencyOwner();
        }

        public boolean isAgencyOwner(){
            return this.role.toUpperCase().equals("ROLE_AGENCY_OWN");
        }


        public boolean isNotStoreOwner(){
            return !isStoreOwner();
        }

        public boolean isStoreOwner(){
            return this.role.toUpperCase().equals("ROLE_STORE_OWN");
        }
    }
}
