<template>
  <el-dialog
    :title="!dataForm.enterpProjId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="企业 id" prop="enterpriseId" v-show="false">
      <el-input v-model="dataForm.enterpriseId" placeholder="企业 id"></el-input>
    </el-form-item>
  <el-form-item label="企业名称" prop="enterpriseName">
    <el-select v-model="dataForm.enterpriseName" filterable placeholder="企业名称" @change="changeName"> 
      <el-option  v-for="n in projectName" :key="n.enterpriseName" :label="n.enterpriseName" :value="n.enterpriseName" ></el-option>
    </el-select>
  </el-form-item>
    <el-form-item label="项目名称" prop="projectName">
      <el-input v-model="dataForm.projectName" placeholder="项目名称"></el-input>
    </el-form-item>
  <el-form-item label="项目时间" prop="projStartTime">
      <el-date-picker
        v-model="dataForm.projStartTime"
        type="date"
        placeholder="项目开始时间"
        format="yyyy-MM-dd">
      </el-date-picker>
    </el-form-item>
        <el-form-item label="截止时间" prop="projStopTime">
          <el-date-picker
              v-model="dataForm.projStopTime"
              type="date"
              placeholder="截止时间"
              format="yyyy-MM-dd">
            </el-date-picker>
        </el-form-item>
    <el-form-item label="项目年度" prop="projectYear">
      <el-input v-model="dataForm.projectYear" placeholder="项目年度"></el-input>
    </el-form-item>
    <el-form-item label="项目负责人" prop="projectDirector">
      <el-input v-model="dataForm.projectDirector" placeholder="项目负责人"></el-input>
    </el-form-item>
    <!-- <el-form-item label="用户id" prop="projectUserId" >
      <el-input v-model="dataForm.projectUserId" placeholder="用户id"></el-input>
    </el-form-item> -->
    <el-form-item label="是否删除" prop="isDel" v-if="dataForm.enterpProjId">
        <el-radio-group v-model="dataForm.isDel">
          <el-radio :label="0">否</el-radio>
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
    </el-form-item>
     <el-form-item label="相关附件" prop="attachLists">
        <el-upload
          class="upload-demo"
          :action="url"
          :data="{enterpriseName: dataForm.enterpriseName,attachType:2}"
          :on-success="successHandle1"
          :on-remove="fileRemoveHandler"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <span v-if="fileIsNull" style="color: crimson">*请上传相关附件</span>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
class EnterpriseAttachment {
  constructor(file) {
    this.name = file.attachName;
    this.url = file.attachPath;
    this.file = file;
  }
}
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        enterpProjId: 0,
        enterpriseId: "",
        enterpriseName: "",
        projectName: "",
        projStartTime: "",
        projStopTime: "",
        projectYear: "",
        projectDirector: "",
        projectUserId: "",
        isDel: 0,
        attachLists: []
      },
      dataRule: {
        enterpriseId: [
          { required: true, message: "企业 id不能为空", trigger: "blur" }
        ],
        enterpriseName: [
          { required: true, message: "企业名称不能为空", trigger: "blur" }
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        projStartTime: [
          { required: true, message: "项目开始时间不能为空", trigger: "blur" }
        ],
        projStopTime: [
          { required: true, message: "截止时间不能为空", trigger: "blur" }
        ],
        projectYear: [
          { required: true, message: "项目年度不能为空", trigger: "blur" }
        ],
        projectDirector: [
          { required: true, message: "项目负责人不能为空", trigger: "blur" }
        ],
        // projectUserId: [
        //   { required: true, message: "用户id不能为空", trigger: "blur" }
        // ],
        isDel: [
          { required: true, message: "是否删除不能为空", trigger: "blur" }
        ],
        attachLists: [
          { required: true, message: "附件不能为空", trigger: "blur" }
        ]
      },
      projectName: [],
      loading: false,
      fileIsNull: false,
      fileList: [],
      url: ""
    };
  },
  methods: {
    init(id) {
      this.url = this.$http.adornUrl(
        `/enterprise/innovateenterpriseattach/upload?token=${this.$cookie.get(
          "token"
        )}`
      );
      this.dataForm.enterpProjId = id || 0;
      this.visible = true;
      this.getProjectName();
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.enterpProjId) {
          this.$http({
            url: this.$http.adornUrl(
              `/enterprise/innovateenterpriseproject/info/${
                this.dataForm.enterpProjId
              }`
            ),
            method: "get",
            params: this.$http.adornParams()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.enterpriseId =
                data.infoModel.projectEntity.enterpriseId;
              this.dataForm.enterpriseName =
                data.infoModel.projectEntity.enterpriseName;
              this.dataForm.projectName =
                data.infoModel.projectEntity.projectName;
              this.dataForm.projStartTime =
                data.infoModel.projectEntity.projStartTime;
              this.dataForm.projStopTime =
                data.infoModel.projectEntity.projStopTime;
              this.dataForm.projectYear =
                data.infoModel.projectEntity.projectYear;
              this.dataForm.projectDirector =
                data.infoModel.projectEntity.projectDirector;
              this.dataForm.projectUserId =
                data.infoModel.projectEntity.projectUserId;
              this.dataForm.isDel = data.infoModel.projectEntity.isDel;
              this.dataForm.attachLists = data.infoModel.attachEntities;
              // 附件回显
              let attachList = [];
              for (let i = 0; i < this.dataForm.attachLists.length; i++) {
                attachList.push(
                  new EnterpriseAttachment(this.dataForm.attachLists[i])
                );
              }
              this.fileList = attachList;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(
              `/enterprise/innovateenterpriseproject/${
                !this.dataForm.enterpProjId ? "save" : "update"
              }`
            ),
            method: "post",
            data: this.$http.adornData({
              projectEntity: {
                enterpProjId: this.dataForm.enterpProjId || undefined,
                enterpriseId: this.dataForm.enterpriseId,
                enterpriseName: this.dataForm.enterpriseName,
                projectName: this.dataForm.projectName,
                projStartTime: this.dataForm.projStartTime,
                projStopTime: this.dataForm.projStopTime,
                projectYear: this.dataForm.projectYear,
                projectDirector: this.dataForm.projectDirector,
                projectUserId: this.dataForm.projectUserId,
                isDel: this.dataForm.isDel
              },
              attachEntities: this.dataForm.attachLists
            })
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                }
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
    getProjectName() {
      this.$http({
        url: this.$http.adornUrl(`/enterprise/innovateenterpriseinfo/nameList`),
        method: "get"
      }).then(({ data }) => {
        debugger;
        if (data && data.code === 0) {
          this.projectName = data.list;
          console.log(this.projectName);
        }
      });
    },
    //根据改变选中值更改企业id
    changeName(query) {
      if (query !== "") {
        let list = this.projectName.filter(item => {
          return item.enterpriseName.indexOf(query) > -1;
        });
        this.dataForm.enterpriseId = list[0].settledEnterpId;
      }
    },
    // 上传成功
    successHandle1(response, file, fileList) {
      if (response && response.code === 0) {
        this.dataForm.attachLists.push(response.innovateEnterpriseAttachEntity);
        this.fileIsNull = false;
      } else {
        this.$message.error(response.msg);
      }
    },
    fileRemoveHandler(file, fileList) {
      // 移除attachList中的附件
      let tempFileList = [];
      for (let index = 0; index < this.dataForm.attachLists.length; index++) {
        if (this.dataForm.attachLists[index].attachName !== file.name) {
          tempFileList.push(this.dataForm.attachLists[index]);
        }
      }
      this.dataForm.attachLists = tempFileList;
    }
  }
};
</script>
