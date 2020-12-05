<template>
  <el-dialog
    :title="!dataForm.settledEnterpId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @close="handleClose">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <el-form-item label="企业名称" prop="enterpriseName">
        <el-input v-model="dataForm.enterpriseName" placeholder="企业名称"></el-input>
      </el-form-item>
      <!-- <el-form-item label="用户ID" prop="enterpriseUserId">
        <el-input v-model="dataForm.enterpriseUserId" placeholder="用户ID"></el-input>
      </el-form-item> -->
      <el-form-item label="企业负责人姓名" prop="enterpriseDirector">
        <el-input v-model="dataForm.enterpriseDirector" placeholder="企业负责人姓名"></el-input>
      </el-form-item>
      <el-form-item label="负责人所在学院" prop="departmentDirector">
        <el-select v-model="dataForm.departmentDirector" placeholder="负责人所在学院" @change="changeName" style="display: unset">
          <el-option v-for="item in instituteList" :key="item.instituteName" :label="item.instituteName"
                     :value="item.instituteName">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="负责人所在学院 id" prop="enterpriseId" v-show="false">
        <el-input v-model="dataForm.enterpriseId" placeholder="企业 id"></el-input>
      </el-form-item>

      <el-form-item label="入驻时间" prop="settledTime">
        <el-date-picker style="width: 100%"
          v-model="dataForm.settledTime"
          type="date"
          placeholder="入驻时间"
          value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="企业类型" prop="enterpriseType">
        <el-select v-model="dataForm.enterpriseType" placeholder="企业类型" style="display: unset">
          <el-option v-for="item in projectTypeList" :key="item.value" :label="item.label" :value="item.label">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="主要经营范围" prop="businessScope">
        <el-input v-model="dataForm.businessScope" placeholder="主要经营范围"></el-input>
      </el-form-item>
      <el-form-item label="相关附件" prop="attachLists">
        <el-upload
          class="upload-demo"
          :action="url"
          :data="{enterpriseName: dataForm.enterpriseName,attachType:1}"
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
  // eslint-disable-next-line no-unused-vars
  class EnterpriseAttachment {
    constructor(file) {
      this.name = file.attachName
      this.url = file.attachPath
      this.file = file
    }
  }

  export default {
    data() {
      return {
        visible: false,
        dataForm: {
          settledEnterpId: 0,
          enterpriseName: '',
          enterpriseUserId: '',
          enterpriseDirector: '',
          departmentDirector: '',
          settledTime: '',
          enterpriseType: '',
          businessScope: '',
          applyStatus: 0,
          isDel: 0,
          attachLists: [],
          instituteId: ''
        },
        dataRule: {
          enterpriseName: [
            {required: true, message: '企业名称不能为空', trigger: 'blur'}
          ],
          enterpriseDirector: [
            {required: true, message: '企业负责人姓名不能为空', trigger: 'blur'}
          ],
          departmentDirector: [
            {required: true, message: '负责人所在学院不能为空', trigger: 'blur'}
          ],
          settledTime: [
            {required: true, message: '入驻时间不能为空', trigger: 'blur'}
          ],
          enterpriseType: [
            {required: true, message: '企业类型不能为空', trigger: 'blur'}
          ],
          businessScope: [
            {required: true, message: '主要经营范围不能为空', trigger: 'blur'}
          ]
        },
        projectTypeList: [
          {value: 1, label: '先进制造业'},
          {value: 2, label: '现代服务业'},
          {value: 3, label: '现代农业'},
          {value: 4, label: '批发零售业'},
          {value: 5, label: '其他'}
        ],
        fileIsNull: false,
        fileList: [],
        url: '',
        instituteList: [],
        noClick: false,
        delAttachLists:[]
      }
    },
    methods: {
      init(id) {
        this.url = this.$http.adornUrl(
          `/enterprise/innovateenterpriseattach/upload?token=${this.$cookie.get(
            'token'
          )}`
        )
        this.dataForm.settledEnterpId = id || 0
        this.visible = true
        this.getInstituteList()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.settledEnterpId) {
            this.$http({
              url: this.$http.adornUrl(
                `/enterprise/innovateenterpriseinfo/info/${
                  this.dataForm.settledEnterpId
                }`
              ),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.enterpriseName =
                  data.infoModel.infoEntity.enterpriseName
                this.dataForm.enterpriseUserId =
                  data.infoModel.infoEntity.enterpriseUserId
                this.dataForm.enterpriseDirector =
                  data.infoModel.infoEntity.enterpriseDirector
                this.dataForm.departmentDirector =
                  data.infoModel.infoEntity.departmentDirector
                this.dataForm.settledTime = data.infoModel.infoEntity.settledTime
                this.dataForm.enterpriseType =
                  data.infoModel.infoEntity.enterpriseType
                this.dataForm.businessScope =
                  data.infoModel.infoEntity.businessScope
                this.dataForm.applyStatus = parseInt(
                  data.infoModel.infoEntity.applyStatus || 0
                )
                this.dataForm.isDel = data.infoModel.infoEntity.isDel
                this.dataForm.instituteId = data.infoModel.infoEntity.instituteId
                this.dataForm.attachLists = data.infoModel.attachEntities

                // 审核点击状态
                if (this.dataForm.applyStatus === 1 || this.dataForm.applyStatus === 2) {
                  this.noClick = true
                }
                // 附件回显
                let attachList = []
                for (let i = 0; i < this.dataForm.attachLists.length; i++) {
                  attachList.push(
                    new EnterpriseAttachment(this.dataForm.attachLists[i])
                  )
                }
                this.fileList = attachList
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(
                `/enterprise/innovateenterpriseinfo/${
                  !this.dataForm.settledEnterpId ? 'save' : 'update'
                }`
              ),
              method: 'post',
              data: this.$http.adornData({
                infoEntity: {
                  settledEnterpId: this.dataForm.settledEnterpId || undefined,
                  enterpriseName: this.dataForm.enterpriseName,
                  enterpriseUserId: this.dataForm.enterpriseUserId,
                  enterpriseDirector: this.dataForm.enterpriseDirector,
                  departmentDirector: this.dataForm.departmentDirector,
                  settledTime: this.dataForm.settledTime,
                  enterpriseType: this.dataForm.enterpriseType,
                  businessScope: this.dataForm.businessScope,
                  applyStatus: this.dataForm.applyStatus,
                  instituteId: this.dataForm.instituteId
                },
                attachEntities: this.dataForm.attachLists
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
              }
            })
            if (this.delAttachEntityList) {
              this.$http({
                url: this.$http.adornUrl(
                  `/enterprise/innovateenterpriseattach/delete`
                ),
                method: "post",
                data: this.$http.adornData(
                  this.delAttachLists, false
                )
              }).then(({data}) => {
                if (data && data.code === 0) {
                }
              });
            }
          }
        })
      },
      // 上传成功
      successHandle1(response, file, fileList) {
        if (response && response.code === 0) {
          this.dataForm.attachLists.push(response.innovateEnterpriseAttachEntity)
          this.fileIsNull = false
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler(file, fileList) {
        // 移除attachList中的附件
        let tempFileList = []
        debugger
        for (let index = 0; index < this.dataForm.attachLists.length; index++) {
          if (this.dataForm.attachLists[index].attachName !== file.name) {
            tempFileList.push(this.dataForm.attachLists[index])
          } else {
            let attachId = this.dataForm.attachLists[index].attachId
            if (attachId) {
              this.delAttachLists.push(attachId)
            }
          }
        }
        this.dataForm.attachLists = tempFileList
      },
      // 二级学院
      getInstituteList() {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.instituteList = data.institute
          }
        })
      },
      // 清空附件列表
      handleClose() {
        this.fileList = []
      },
      //根据改变选中值更改学院id
      changeName(query) {
        if (query !== "") {
          let list = this.instituteList.filter(item => {
            return item.instituteName.indexOf(query) > -1;
          });
          this.dataForm.instituteId = list[0].instituteId;
        }
      },
    }
  }
</script>
