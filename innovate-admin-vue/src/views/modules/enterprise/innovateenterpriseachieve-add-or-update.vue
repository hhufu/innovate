<template>
  <el-dialog
    :title="!dataForm.enterpAchieveId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @close="handleClose">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="12rem" style="width: 94%; margin: 0 auto">
      <el-form-item label="企业名称" prop="enterpriseName">
        <el-select v-model="dataForm.enterpriseName" filterable placeholder="企业名称" style="width: 100%" @change="changeName">
          <el-option v-for="n in projectName" :key="n.enterpriseName" :label="n.enterpriseName"
                     :value="n.enterpriseName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="负责人" prop="enterpriseDirector">
        <el-input v-model="dataForm.enterpriseDirector" placeholder="负责人"></el-input>
      </el-form-item>
      <el-form-item label="获奖名称" prop="awardProjectName">
        <el-input v-model="dataForm.awardProjectName" placeholder="获奖名称（项目名称）"></el-input>
      </el-form-item>
      <el-form-item label="获奖时间" prop="awardTime">
        <el-date-picker v-model="dataForm.awardTime" type="date" value-format="yyyy-MM-dd HH:mm:ss" format="yyyy-MM-dd"
                        placeholder="选择日期"/>
      </el-form-item>
      <el-form-item label="类型" prop="awardProjectType">
        <el-select v-model="dataForm.awardProjectType" placeholder="类型（应用成果转化/获奖/著作权/企业证书）" style="width: 100%"
                   @change="changeTypeId">
          <el-option v-for="n in awardProjectType" :key="n.awardProjectType" :label="n.awardProjectType"
                     :value="n.awardProjectType"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属二级学院" prop="instituteId">
<!--        <el-select v-model="dataForm.instituteId" placeholder="所属二级学院" style="width: 100%" @change="changeInstituteId"-->
        <el-select v-model="dataForm.instituteId" placeholder="所属二级学院" style="width: 100%" disabled>
                   disabled>
          <el-option v-for="n in instituteName" :key="n.instituteName" :label="n.instituteName"
                     :value="n.instituteName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="附件要求">
        <template>
          <el-alert
            title=""
            type="success"
            :closable="false"
            :description="fileAskContent">
          </el-alert>
        </template>
      </el-form-item>
      <el-form-item label="相关附件" prop="attachLists">
        <el-upload
          class="upload-demo"
          :action="url"
          :data="{enterpriseName: dataForm.enterpriseName,attachType:3}"
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
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading" :disabled="loading">确定</el-button>
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
        loading: false,
        visible: false,
        fileAskContent: '无',// 附件要求
        delAttachLists: [],
        attachLists: [],
        dataForm: {
          enterpAchieveId: 0,
          enterpriseId: '',
          enterpriseName: '',
          enterpriseDirector: '',
          enterpriseUserId: '',
          awardProjectName: '',
          awardTime: '',
          awardProjectType: '',
          instituteId: '',
          applyStatus: 9,
          isDel: ''
        },
        dataRule: {
          enterpriseName: [
            {required: true, message: '企业名称不能为空', trigger: 'blur'}
          ],
          enterpriseDirector: [
            {required: true, message: '负责人不能为空', trigger: 'blur'}
          ],
          awardProjectName: [
            {required: true, message: '获奖名称（项目名称）不能为空', trigger: 'blur'}
          ],
          awardTime: [
            {required: true, message: '获奖（得）时间不能为空', trigger: 'blur'}
          ],
          awardProjectType: [
            {required: true, message: '类型（应用成果转化/获奖/著作权/企业证书）不能为空', trigger: 'blur'}
          ],
          institute: [
            {required: true, message: '所属二级学院不能为空', trigger: 'blur'}
          ]
        },
        projectName: [],
        instituteName: [],
        awardProjectType: [],
        awardProjectTypeId: [],
        institute: [],
        url: "",
        fileIsNull: false,
        fileList: [],
      }
    },
    methods: {
      init(id) {
        this.loading = false
        this.url = this.$http.adornUrl(
          `/enterprise/innovateenterpriseattach/upload?token=${this.$cookie.get(
            "token"
          )}`
        );
        this.dataForm.enterpAchieveId = id || 0
        this.visible = true
        this.getProjectName()
        this.getInstituteName()
        this.getFileAsk()
        this.getTypeName()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.enterpAchieveId) {
            this.$http({
              url: this.$http.adornUrl(`/enterprise/innovateenterpriseachieve/info/${this.dataForm.enterpAchieveId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.enterpriseId = data.achieveModel.achieveEntity.enterpriseId
                this.dataForm.enterpriseName = data.achieveModel.achieveEntity.enterpriseName
                this.dataForm.enterpriseDirector = data.achieveModel.achieveEntity.enterpriseDirector
                this.dataForm.enterpriseUserId = data.achieveModel.achieveEntity.enterpriseUserId
                this.dataForm.awardProjectName = data.achieveModel.achieveEntity.awardProjectName
                this.dataForm.awardTime = data.achieveModel.achieveEntity.awardTime
                this.dataForm.awardProjectTypeId = data.achieveModel.achieveEntity.awardProjectType
                this.dataForm.awardProjectType = data.achieveModel.achieveEntity.typeName
                this.dataForm.institute = data.achieveModel.achieveEntity.instituteId
                this.dataForm.instituteId = data.achieveModel.achieveEntity.instituteName
                this.dataForm.isDel = data.achieveModel.achieveEntity.isDel
                this.attachLists = data.achieveModel.attachEntities
                // 附件回显
                let attachList = [];
                for (let i = 0; i < this.attachLists.length; i++) {
                  attachList.push(
                    new EnterpriseAttachment(this.attachLists[i])
                  );
                }
                this.fileList = attachList;
              }
            })
          } else {
            this.reset();
          }
        })
      },
      // 重置
      reset() {
        this.dataForm = {
          enterpAchieveId: 0,
          enterpriseId: '',
          enterpriseName: '',
          enterpriseDirector: '',
          enterpriseUserId: '',
          awardProjectName: '',
          awardTime: '',
          awardProjectType: '',
          instituteId: '',
          isDel: '',
          applyStatus: 9,
        };
        this.projectName = []
        this.instituteName = []
        this.awardProjectType = []
        this.awardProjectTypeId = []
        this.institute = []
        this.attachLists = []
        this.delAttachLists = []
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(
                `/enterprise/innovateenterpriseachieve/${
                  !this.dataForm.enterpAchieveId ? 'save' : 'update'
                  }`
              ),
              method: 'post',
              data: this.$http.adornData({
                achieveEntity: {
                  enterpAchieveId: this.dataForm.enterpAchieveId || undefined,
                  enterpriseId: this.dataForm.enterpriseId,
                  enterpriseName: this.dataForm.enterpriseName,
                  enterpriseDirector: this.dataForm.enterpriseDirector,
                  enterpriseUserId: this.dataForm.enterpriseUserId,
                  awardProjectName: this.dataForm.awardProjectName,
                  awardTime: this.dataForm.awardTime,
                  awardProjectType: this.dataForm.awardProjectTypeId,
                  instituteId: this.dataForm.institute,
                  isDel: this.dataForm.isDel,
                  applyStatus: this.dataForm.applyStatus
                },
                attachEntities: this.attachLists,
                delAttachLists: this.delAttachLists
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
                this.loading = false
              }
            })
          }
        })
      },
      // 上传成功
      successHandle1(response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.innovateEnterpriseAttachEntity)
          this.fileIsNull = false;
        } else {
          this.$message.error(response.msg);
        }
      },
      fileRemoveHandler(file, fileList) {
        // 移除attachList中的附件
        let tempFileList = [];
        for (let index = 0; index < this.attachLists.length; index++) {
          if (this.attachLists[index].attachName !== file.name) {
            tempFileList.push(this.attachLists[index]);
          } else {
            this.delAttachLists.push(this.attachLists[index])
          }
        }
        this.attachLists = tempFileList;
      },
      //企业名字
      getProjectName() {
        this.$http({
          url: this.$http.adornUrl(`/enterprise/innovateenterpriseinfo/nameList`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.projectName = data.list
          }
        })
      },
      //学院
      getInstituteName() {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.instituteName = data.institute
          }
        })
      },
      getTypeName() {
        this.$http({
          url: this.$http.adornUrl(`/enterprise/innovateawardprojecttype/typeNameList`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.awardProjectType = data.list
          }
        })
      },
      getFileAsk() {
        // 获取文件要求：类型=>1 大创,2 中期检查,3 赛事,4 结题,5 企业成果
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/file/ask/query`),
          method: 'get',
          params: this.$http.adornParams({
            'fileAskType': 6,
            'fileAskTime': new Date().getFullYear()
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.fileAskContent = data.fileAsk == null ? '无' : data.fileAsk.fileAskContent
            this.dataListLoading = false
          }
        })
      },
      changeName(query) {
        if (query !== '') {
          let list = this.projectName.filter(item => {
            return item.enterpriseName
              .indexOf(query) > -1
          });
          this.dataForm.enterpriseId = list[0].settledEnterpId;
          debugger
          this.dataForm.institute = list[0].instituteId;

          let instituteIds = this.instituteName.filter(item => {
            return item.instituteId === parseInt(this.dataForm.institute)

          })
          this.dataForm.instituteId = instituteIds[0].instituteName;
        }
      },
      changeTypeId(query) {
        if (query !== '') {
          let list = this.awardProjectType.filter(item => {
            return item.awardProjectType
              .indexOf(query) > -1
          })
          this.dataForm.awardProjectTypeId = list[0].awardProjectTypeId
        }
      },
      // changeInstituteId(query) {
      //   debugger
      //   if (query !== '') {
      //     let list = this.instituteName.filter(item => {
      //       return item.instituteName
      //         .indexOf(query) > -1
      //     })
      //     this.dataForm.institute = list[0].instituteId
      //   }
      // },
      // 清空附件列表
      handleClose() {
        this.fileList = []
      }

    }
  }
</script>
