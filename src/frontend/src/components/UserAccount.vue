<template>
  <div class="background-content">
    <div class="title">
      <label>User Accounts</label>
    </div>
    <div>
      <button class="create-button" @click="isCreateUserModalVisibility = true">
        <span class="plus-icon"><i class="fa fa-plus"></i></span>
        Create
      </button>
    </div>
    <div class="row">
      <div class="col-12 col-md-6" v-for="user in userList" :key="user">
        <div class="user-card row">
          <div class="col-12 col-md-5">
            <img class="avatar" alt="" :src="user.avatar">
          </div>
          <div class="col-12 col-md-6">
            <div class="card-content">{{user.firstName}} {{user.lastName}}</div>
            <div class="card-content">{{user.email}}</div>
          </div>
          <div class="col-12 col-md-1">
            <div class="fa-icon" @click="fetchData(user);this.isCreateUserModalVisibility = true; this.viewOnly = true;"><i class="fa fa-eye"></i></div>
            <div class="fa-icon" @click="fetchData(user);this.isCreateUserModalVisibility = true;this.isUpdateUser = true;"><i class="fa fa-pen"></i></div>
            <div class="fa-icon" @click="fetchData(user);this.isDeleteUserModalVisibility = true;"><i class="fa fa-times"></i></div>
          </div>
        </div>
      </div>
    </div>

    <!--    create user-->
    <div class="user-modal" v-if="isCreateUserModalVisibility">
      <div class="user-content">
        <div><h4 class="modal-title" v-if="!(isUpdateUser || viewOnly)">Create User</h4>
        <div><h4 class="modal-title" v-if="isUpdateUser">Update User</h4>
        <div><h4 class="modal-title" v-if="viewOnly">View User</h4>
          <span class="close-icon" @click="reset();isCreateUserModalVisibility = false;"><i class="fa fa-times"></i></span>

        </div>
        <input class="form-control" type="text" v-model="user.firstName" placeholder="First Name" :readonly="viewOnly">
        <input class="form-control" type="text" v-model="user.lastName" placeholder="Last Name" :readonly="viewOnly">
        <input class="form-control" type="text" v-model="user.email" placeholder="Email" :readonly="viewOnly">
        <div>
          <label>Avatar</label>
        </div>
        <div>
          <input type="file" @change="onFileChange" :disabled="viewOnly">
          <img class="avatar-img" :src="user.avatar" alt=""/>
        </div>
        <div class="error-message" v-if="errorMessage || errorMessage !== ''">
          {{errorMessage}}
        </div>
        <div>
          <button v-if="!isUpdateUser" class="submit-button" @click="createUser();" :disabled="!(user.firstName && user.lastName && user.email && user.avatar) || viewOnly">Submit</button>
          <button v-if="isUpdateUser" class="submit-button" @click="updateUser(user.id);" :disabled="!(user.firstName && user.lastName && user.email && user.avatar) || viewOnly">Submit</button>
        </div>
      </div>
    </div>
    </div>
    </div>

    <!--    delete user-->
    <div class="user-modal" v-if="isDeleteUserModalVisibility">
      <div class="user-content">
        <div class="text-center"><h4 class="modal-title delete-title">Are you sure want to delete this user {{user.firstName}} {{user.lastName}}</h4>
          <span class="close-icon" @click="reset();isDeleteUserModalVisibility = false;"><i class="fa fa-times"></i></span>

        </div>
        <div class="button-flex">
          <button class="cancel-button" @click="isDeleteUserModalVisibility = false;">Cancel</button>
          <button class="delete-button" @click="deleteUser(user.id); isDeleteUserModalVisibility = false">Delete</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: 'UserAccount',
  data() {
    return {
      msg: '',
      userList: [],
      img: null,
      isCreateUserModalVisibility: false,
      isDeleteUserModalVisibility: false,
      isUpdateUserModalVisibility: false,
      errorMessage: null,
      user:{
        id: 0,
        firstName: null,
        lastName: null,
        email: null,
        avatar: null,
      },
      show:true,
      isUpdateUser: false,
      filterFirstName: null,
      filterLastName: null,
      filterEmail: null,
      viewOnly: false,
      maximumId: 0

    }
  },
  methods: {
    fetch() {
      fetch("/api/user/user-list")
          .then((response) => response.json())
          .then((data) => {
            this.userList = data;
            let ids = data.map(x => Number(x.id));
            this.maximumId = Math.max(...ids);
          });
    },

    createUser() {
      this.user.id = this.maximumId+1;
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(this.user)
      };
      if(this.user.firstName && this.user.lastName && this.user.email && this.user.avatar){
        fetch("/api/user/create-user", requestOptions)
            .then((response) => response.text())
            .then((data)=>{
              if(!data){
                this.isCreateUserModalVisibility = false;
                this.fetch();
                this.reset();
              }
              else{
                this.errorMessage = data;
                this.isCreateUserModalVisibility = true;
              }

            })

      }
    },
    onFileChange(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;
      this.createImage(files[0]);
    },
    createImage(file) {
      this.img = new Image();
      const reader = new FileReader();

      reader.onload = (e) => {
        this.user.avatar = e.target.result;
      };
      reader.readAsDataURL(file);
    },

    deleteUser(id){
      this.axios.delete(`/api/user/delete-user-by-id/${id}`).then().then(()=>{
        this.fetch();
        this.reset();
      })

    },
    updateUser(id){
      /*this.axios.post(`/api/user/update-user-by-id/${id}`, { // <== use axios.post
        data: this.user,
        _method: 'patch'
      });*/
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(this.user)
      };
      if(this.user.firstName && this.user.lastName && this.user.email && this.user.avatar){
        fetch(`/api/user/update-user-by-id/${id}`, requestOptions)
            .then((response) => response.text())
            .then((data)=>{
              if(!data){
                this.isCreateUserModalVisibility = false;
                this.fetch();
                this.reset();
              }
              else{
                this.errorMessage = data;
                this.isCreateUserModalVisibility = true;
              }

            })

      }
    },
    fetchData(user){
      this.user.id = user.id;
      this.user.firstName = user.firstName;
      this.user.lastName = user.lastName;
      this.user.email = user.email;
      this.user.avatar = user.avatar;

    },
    reset(){
      this.user.firstName = null;
      this.user.lastName = null;
      this.user.email = null;
      this.user.avatar = null;
      this.isUpdateUser = false;
      this.errorMessage = null;
      this.viewOnly = false;
      this.isCreateUserModalVisibility = false;
      this.isDeleteUserModalVisibility = false;
    }
  },
  mounted() {
    this.fetch();
  }
}
</script>

<style>

.create-button{
  background-color: #0983E6;
  border-radius: 15px;
  border-color: transparent;
  padding: 1% 4% 1% 4%;
  color: white;
  font-size: 25px;
  display: flex;
  margin-bottom: 5%;
  position: relative;
  width: 40%;
  justify-content: center;
}

.form-control{
  font-size: 20px;
}

.plus-icon{
  position: absolute;
  top: 11%;
  left: 5%;
}

.title{
  font-size: 50px;
  font-weight: bold;
  margin: 3% 0;
}

.form-control{
  margin: 5% 0;
  border-width: 0 0 1px;
}

.submit-button, .cancel-button, .delete-button{
  margin: 5% auto;
  display: flex;
  color: white;
  font-size: 25px;
  border: none;
  border-radius: 20px;
  padding: 2% 11%;
}

.submit-button{
  margin: 10px auto;
  background-color: #0983E6;
}

.cancel-button{
  background-color: darkgray;
}

.delete-button{
  background-color: #cc3333;
}

.submit-button:disabled{
  background-color: lightgray;
}

.avatar-img{
  width: 100%;
  margin: 5% auto;
  height: 250px;
  object-fit: contain;
}

.avatar{
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.user-card{
  border: 1px solid darkgray;
  margin: 5%;
  padding: 3% 5%;
  border-radius: 25px;
}

.fa{
  margin-bottom: 2%;
}

.fa-icon{
  height: 40px;
  cursor: pointer;
}

.background-content{
  position: relative;
}

.user-modal{
  position: fixed;
  background: rgba(0,0,0,.5);
  top: 0;
  width: 100%;
  left: 0;
  height: 100%;
}

.user-content{
  position: relative;
  background: white;
  width: 50%;
  padding: 5% 2%;
  border-radius: 25px;
  height: 80%;
  overflow-y: scroll;
  margin: 5% auto;
}

.close-icon{
  position: absolute;
  right: 20px;
  top: 20px;
  cursor: pointer;
}

.button-flex{
  display: flex;
  justify-content: space-around;
}

.error-message{
  color: red;
}

.card-content{
  margin: 2%;
  font-size: 23px;
  font-weight: 600;
}

.user-content label{
  font-size: 20px;
}

@media (min-width:1000px){
  .create-button{
    width: 20%;
  }

  .plus-icon{
    top: 19%;
  }
}


@media (min-width: 768px) and (max-width: 1200px){
  .user-card.row{
    display: block;
    text-align: center;
  }

  .user-card .col-md-1, .user-card .col-md-5, .user-card .col-md-6{
    max-width: 100%;
  }
}

@media (max-width: 1200px) {
  .user-card{
    position: relative;
  }

  .user-card .col-md-1{
    position: absolute;
    top: 5%;
    left: 80%;
    width: 20%;
  }
}

@media (max-width: 950px){
  .user-content{
    width: 75%;
  }
}

@media (max-width: 767px){
  .user-card{
    text-align: center;
  }

  .title{
    font-size: 30px;
  }
}

@media (max-width: 600px){
  .create-button{
    font-size: 18px;
  }

  img{
    width: 100%;
  }

  .user-content{
    height: 100%;
    width: unset;
  }

  .submit-button{
    margin: auto;
  }

  .delete-title{
    width: 88%;
  }

  .form-control{
    font-size: 16px;
  }

  .user-content label{
    font-size: 16px;
  }
}

@media (max-width: 450px){
  .create-button{
    width: 43%;
  }
}

@media (max-width: 400px){
  .card-content{
    font-size: 20px;
  }

  .avatar{
    width: 100px;
    height: 100px;
  }
}
</style>