<template>
  <el-dialog
    :title="!dataForm.professAchieveId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="120px">
      <!--    <el-form-item label="负责人id" prop="professUserId">-->
      <!--      <el-input v-model="dataForm.professUserId" placeholder="负责人id"></el-input>-->
      <!--    </el-form-item>-->
      <el-form-item label="所属二级学院" prop="instituteId">
        <el-select v-model="dataForm.instituteId" placeholder="负责人所在学院">
          <el-option v-for="item in instituteName" :key="item.instituteId" :label="item.instituteName"
                     :value="item.instituteId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="获奖名称" prop="awardAchieName">
        <el-input v-model="dataForm.awardAchieName" placeholder="获奖名称" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="负责人" prop="professAchieDirector">
        <el-input v-model="dataForm.professAchieDirector" placeholder="负责人" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="团队成员" prop="teamMenber">
        <el-input v-model="dataForm.teamMenber" placeholder="团队成员" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="成果类型" prop="professAchieveType">
        <el-select v-model="dataForm.professAchieveType" placeholder="请选择成果类型" @change="changeProfessAchieveType">
          <el-option v-for="item in professAchieveType" :key="item.professAchieveType" :label="item.professAchieveType"
                     :value="item.professAchieveType">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="获得时间" prop="awardTime">
        <el-date-picker
          v-model="dataForm.awardTime"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="获得时间">
        </el-date-picker>
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
      <!--独立附件start-->
      <el-form-item label="相关附件" prop="reportSalesName">
        <el-upload
          class="upload-demo"
          :action="url"
          :data="{textType: textType}"
          :on-success="successHandle1"
          :on-remove="fileRemoveHandler"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <span v-if="fileList.length === 0" style="color: crimson">*请上传相关附件</span>
        </el-upload>
      </el-form-item>
      <!--独立附件end-->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>

  class FinishAttachment {
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
        url: '',
        fileList: [], // 文件列表
        attachLists: [],
        fileAskContent: '无', // 附件要求
        dataForm: {
          professAchieveId: 0,
          professUserId: '',
          instituteId: '',
          awardAchieName: '',
          professAchieDirector: '',
          teamMenber: '',
          awardTime: '',
          professAchieveType: '',
          professAchieveTypeId: '',
          isDel: ''
        },
        dataRule: {
          instituteId: [
            {required: true, message: '所属二级学院不能为空', trigger: 'blur'}
          ],
          awardAchieName: [
            {required: true, message: '获奖(项目）名称不能为空', trigger: 'blur'}
          ],
          professAchieDirector: [
            {required: true, message: '负责人不能为空', trigger: 'blur'}
          ],
          teamMenber: [
            {required: true, message: '团队成员不能为空', trigger: 'blur'}
          ],
          awardTime: [
            {required: true, message: '获得时间不能为空', trigger: 'blur'}
          ],
          professAchieveType: [
            {required: true, message: '专创成果类型不能为空', trigger: 'blur'}
          ]

        },
        instituteName: [], // 学院列表
        professAchieveType: [], // 成果类型列表
        textType: ''
      }
    },
    methods: {
      init(id) {
        this.url = this.$http.adornUrl(`/profess/attach/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.professAchieveId = id || 0
        this.visible = true
        this.getInstituteName()
        this.getprofessAchieveType()
        this.getFileAsk()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.professAchieveId) {
            this.$http({
              url: this.$http.adornUrl(`/profess/innovateprofessachieve/info/${this.dataForm.professAchieveId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm.professUserId = data.professAchieveEntity.professUserId
                this.dataForm.instituteId = data.professAchieveEntity.instituteId
                this.dataForm.awardAchieName = data.professAchieveEntity.awardAchieName
                this.dataForm.professAchieDirector = data.professAchieveEntity.professAchieDirector
                this.dataForm.teamMenber = data.professAchieveEntity.teamMenber
                this.dataForm.awardTime = data.professAchieveEntity.awardTime
                this.dataForm.professAchieveType = data.professAchieveEntity.professAchieveType
                this.dataForm.professAchieveTypeId = data.professAchieveEntity.professAchieveTypeId
                this.attachLists = data.professAttachEntities
                // 附件回显
                let tempFinishAtta = []
                for (var i = 0; i < this.attachLists.length; i++) {
                  tempFinishAtta.push(new FinishAttachment(this.attachLists[i]))
                }
                this.fileList = tempFinishAtta
              }
            })
          } else {
            this.attachLists = []
            this.fileList = [] // 文件列表
          }
        })
      },
      getFileAsk () {
        // 获取文件要求：类型=>1 大创,2 中期检查,3 赛事,4 结题
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/file/ask/query`),
          method: 'get',
          params: this.$http.adornParams({
            'fileAskType': 9,
            'fileAskTime': new Date().getFullYear()
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.fileAskContent = data.fileAsk == null ? '无' : data.fileAsk.fileAskContent
            this.dataListLoading = false
          }
        })
      },
      changeProfessAchieveType(e) {
        let list = this.professAchieveType.filter(item => {
          return item.professAchieveType.indexOf(e)
        })
        this.dataForm.professAchieveTypeId = list[0].professAchieveTypeId
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid && this.attachLists.length > 0) {
            this.$http({
              url: this.$http.adornUrl(`/profess/innovateprofessachieve/${!this.dataForm.professAchieveId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                professAchieveEntity: this.dataForm,
                attachEntityList: this.attachLists,
                delAttachEntityList: this.delAttachLists
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
          }
        })
      },
      getInstituteName() {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            this.instituteName = data.institute
          }
        })
      },
      getprofessAchieveType() {
        this.$http({
          url: this.$http.adornUrl(`/profess/innovateprofessachievetype/all`),
          method: 'get'
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            this.professAchieveType = data.professAchieveType
          }
        })
      },
      // 上传成功
      successHandle1(response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.professAttachEntity)
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler(file, fileList) {
        // 移除attachList中的附件
        let tempFileList = []
        for (var index = 0; index < this.attachLists.length; index++) {
          if (this.attachLists[index].attachName !== file.name) {
            tempFileList.push(this.attachLists[index])
          } else {
            this.delAttachLists.push(this.attachLists[index])
          }
        }
        this.attachLists = tempFileList
      }
    }
  }
</script>
