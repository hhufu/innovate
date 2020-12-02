<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <table border="1" cellspacing="0" width="100%" class="table">
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr class="contents" align="center">
        <th colspan="10" style="height: 3.2rem">
          积分申请信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">申请人</th>
        <td colspan="3">{{userEntity.name}}</td>
        <th colspan="2">学号</th>
        <td colspan="3">{{dataForm.stuNum}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">申请类型</th>
        <td colspan="8">{{dataForm.participateType}}</td>
      </tr>
      <tr align="center">
        <th colspan="2">项目名称</th>
        <td colspan="8">{{dataForm.raceGrade}}</td>
      </tr>
      <tr align='center' v-if="dataForm.persionType != '' && dataForm.persionType != null">
        <th colspan="2">参与人类别</th>
        <td colspan="8">{{dataForm.persionType? (dataForm.persionType == 1 ?'负责人':'参与成员'): '-'}}</td>
      </tr>
      <tr align='center' v-if="dataForm.prizeGrade != ''">
        <th colspan="2">奖项等级</th>
        <td colspan="8">{{dataForm.prizeGrade}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">申请积分</th>
        <td colspan="8">{{dataForm.applyIntegral}}积分</td>
      </tr>
      <!--附件开始-->
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align="center" class="contents">
        <th colspan="10" style="height: 3.2rem">附件</th>
      </tr>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align='center'>
        <th colspan="7">附件名</th>
        <td colspan="3">操作</td>
      </tr>
      <template>
        <tr v-for="item in attachLists"
            align="center">
          <td colspan="7">{{item.attachName}}</td>
          <td colspan="3"><el-button @click="attachDown(item)" :loading="downloadLoading"><span v-text="downloadText"></span></el-button></td>
        </tr>
      </template>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <!--附件结束-->
    </table>
  </el-dialog>
</template>

<script>

  export default {
    data() {
      return {
        visible: false,
        downloadLoading: false,
        downloadText: '下载',
        attachLists: [], // 附件列表
        parentId: 0, // 父id
        participateList: [],
        integral: 0, // 该项目申请积分
        integralTo: 0, // 积分差值
        fileAskContent: '无',
        delAttachLists: [], // 要删除的附件
        userEntity: {}, // 用戶信息
        url: '',
        noEditPoint: true,
        fileIsNull: true,
        raceGradeList: [],
        prizeGradeList: [],
        persionTypeOptions: [{lable: '负责人', value: 1}, {lable: '参与人员', value: 2}],
        dataForm: {
          integralApplyId: 0,
          stuNum: '',
          applyTime: '',
          isDel: '',
          applyIntegral: '',
          persionType: '',
          prizeGrade: '',
          raceGrade: '',
          participateType: '',
          sysPointsId: 0,
          applyUserId: 0
        },
        textType: '',
        dataRule: {
          applyTime: [
            {required: true, message: '申请时间不能为空', trigger: 'blur'}
          ],
          isDel: [
            {required: true, message: '是否删除不能为空', trigger: 'blur'}
          ],
          applyIntegral: [
            {required: true, message: '积分不能为空', trigger: 'blur'}
          ],
          raceGrade: [
            {required: true, message: '项目名称不能为空', trigger: 'blur'}
          ],
          prizeGrade: [
            {required: true, message: '奖项等级不能为空', trigger: 'blur'}
          ],
          participateType: [
            {required: true, message: '参与人类别不能为空', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      init(id) {
        this.url = this.$http.adornUrl(`/points/attach/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.integralApplyId = id || 0
        this.getTypeList()
        this.visible = true
        this.$nextTick(() => {
          if (this.dataForm.integralApplyId) {
            this.$http({
              url: this.$http.adornUrl(`/points/innovatestudentpointsapply/info/${this.dataForm.integralApplyId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.applyTime = data.innovateStudentPointsApply.applyTime
                this.dataForm.sysPointsId = data.innovateStudentPointsApply.sysPointsId
                this.dataForm.isDel = data.innovateStudentPointsApply.isDel
                this.dataForm.applyIntegral = data.innovateStudentPointsApply.applyIntegral
                this.dataForm.persionType = data.innovateStudentPointsApply.persionType
                this.dataForm.prizeGrade = data.innovateStudentPointsApply.prizeGrade
                this.dataForm.raceGrade = data.innovateStudentPointsApply.raceGrade
                this.dataForm.participateType = data.innovateStudentPointsApply.participateType
                this.dataForm.applyUserId = data.innovateStudentPointsApply.applyUserId
                this.dataForm.stuNum = data.innovateStudentPointsApply.stuNum
                this.attachLists = data.attachEntityList
                this.userEntity = data.innovateStudentPointsApply.userEntity
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
          integralApplyId: 0,
          stuNum: '',
          applyTime: '',
          isDel: '',
          applyIntegral: '',
          persionType: '',
          prizeGrade: '',
          raceGrade: '',
          participateType: '',
          sysPointsId: 0,
          applyUserId: 0
        }
      },
      getTypeList () {
        this.participateList = []
        this.$http({
          url: this.$http.adornUrl('/points/innovatesyspoints/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 1000,
            'parentId': 0,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.participateList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.participateList = []
            this.totalPage = 0
          }
        })
      },
      // 文件下载
      attachDown (attach) {
        this.downloadLoading = true
        this.downloadText = '正在下载'
        this.$notify({
          title: '下载提示',
          message: '文件正在后台下载,请您稍后!',
          duration: 0,
          type: 'success'
        })
        this.$httpFile({
          url: this.$httpFile.adornUrl(`/points/attach/download`),
          method: 'post',
          params: this.$httpFile.adornParams({
            'filePath': attach.attachPath
          })
        }).then(response => {
          if (!response) {
            this.$notify({
              title: '下载提示',
              message: '下载失败',
              duration: 0,
              type: 'error'
            })
            this.downloadLoading = false
            return
          }
          let url = window.URL.createObjectURL(new Blob([response.data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', attach.attachName)
          document.body.appendChild(link)
          link.click()
          this.$notify({
            title: '下载成功',
            message: '后台文件下载成功',
            duration: 0,
            type: 'success'
          })
          this.downloadText = '下载'
          this.downloadLoading = false
        }).catch(err => {
          console.log(err)
          this.downloadLoading = false
        })
      }
    }
  }
</script>
