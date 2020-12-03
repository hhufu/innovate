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
          专创结合成果信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">所属二级学院</th>
        <td colspan="3">{{formatInstitute(dataForm.instituteId)}}</td>
        <td colspan="5"></td>
      </tr>
      <tr align='center'>
        <th colspan="2">获奖名称</th>
        <td colspan="3">{{dataForm.awardAchieName}}</td>
        <th colspan="5">成果类型</th>
      </tr>
      <tr align="center">
        <th colspan="2">负责人</th>
        <td colspan="3">{{dataForm.professAchieDirector}}</td>
        <td colspan="5">{{dataForm.professAchieveType}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">团队成员</th>
        <td colspan="3">{{dataForm.teamMenber}}</td>
        <td colspan="5"></td>
      </tr>
      <tr align='center'>
        <th colspan="2">获得时间</th>
        <td colspan="3">{{parseTime(dataForm.awardTime, '{y}年{m}月{d}日')}}</td>
        <td colspan="5"></td>
      </tr>
      <!--附件开始-->
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align="center" class="contents">
        <th colspan="10" style="height: 2.2rem">附件</th>
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
          <td colspan="3">
            <el-button @click="attachDown(item)" :loading="downloadLoading"><span v-text="downloadText"></span>
            </el-button>
          </td>
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
        downloadLoading: false,
        downloadText: '下载',
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
        instituteList: [], // 学院列表
        professAchieveType: [], // 成果类型列表
        textType: ''
      }
    },
    methods: {
      init(id) {
        this.url = this.$http.adornUrl(`/profess/profess/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.professAchieveId = id || 0
        this.visible = true
        this.getInstituteName()
        this.$nextTick(() => {
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
      formatInstitute(e) {
        if (e !== '') {
          for (let i in this.instituteList) {
            if (this.instituteList[i].instituteId == e) {
              return this.instituteList[i].instituteName
            }
          }
        }
        return ''
      },
      getInstituteName() {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.instituteList = data.institute
          }
        })
      },
      // 文件下载
      attachDown(attach) {
        this.downloadLoading = true
        this.downloadText = '正在下载'
        this.$notify({
          title: '下载提示',
          message: '文件正在后台下载,请您稍后!',
          duration: 0,
          type: 'success'
        })
        this.$httpFile({
          url: this.$httpFile.adornUrl(`/profess/attach/download`),
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
